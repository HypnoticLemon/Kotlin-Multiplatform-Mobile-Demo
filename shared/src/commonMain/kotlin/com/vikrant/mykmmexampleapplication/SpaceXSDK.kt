package com.vikrant.mykmmexampleapplication

import com.vikrant.mykmmexampleapplication.cache.Database
import com.vikrant.mykmmexampleapplication.cache.DatabaseDriverFactory
import com.vikrant.mykmmexampleapplication.entity.RocketLaunch
import com.vikrant.mykmmexampleapplication.fileSystem.FileUtils
import com.vikrant.mykmmexampleapplication.network.SpaceXApi


class SpaceXSDK (databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class) suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        FileUtils().writeFile("SpaceXSDK:getLaunches")
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            FileUtils().writeFile("SpaceXSDK:load from DB")
            cachedLaunches
        } else {
            FileUtils().writeFile("SpaceXSDK:load from API")
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}