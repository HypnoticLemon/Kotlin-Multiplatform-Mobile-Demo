package com.vikrant.mykmmexampleapplication.android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vikrant.mykmmexampleapplication.SpaceXSDK
import com.vikrant.mykmmexampleapplication.cache.DatabaseDriverFactory
import com.vikrant.mykmmexampleapplication.fileSystem.FileUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val mainScope = MainScope()

    private lateinit var launchesRecyclerView: RecyclerView
    private lateinit var btnGoToNext: Button
    private lateinit var progressBarView: FrameLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val sdk = SpaceXSDK(DatabaseDriverFactory(this))

    private val launchesRvAdapter = LaunchesRvAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "KMM Example Application"
        setContentView(R.layout.activity_main)

        writeLogs("MainActivity:onCreate")

        launchesRecyclerView = findViewById(R.id.launchesListRv)
        progressBarView = findViewById(R.id.progressBar)
        swipeRefreshLayout = findViewById(R.id.swipeContainer)
        btnGoToNext = findViewById(R.id.btnGoToNext)

        launchesRecyclerView.adapter = launchesRvAdapter
        launchesRecyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            displayLaunches(true)
        }

        displayLaunches(false)

        btnGoToNext.setOnClickListener {
            startActivity(Intent(this, FileReadActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayLaunches(needReload: Boolean) {
        writeLogs("MainActivity:displayLaunches")
        progressBarView.isVisible = true
        mainScope.launch {
            kotlin.runCatching {
                sdk.getLaunches(needReload)
            }.onSuccess {
                writeLogs("MainActivity:displayLaunches::onSuccess")
                launchesRvAdapter.launches = it
                launchesRvAdapter.notifyDataSetChanged()
            }.onFailure {
                writeLogs("MainActivity:displayLaunches::onFailure")
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            progressBarView.isVisible = false
        }
    }

    private fun writeLogs(fileContent: String) {
        FileUtils().writeFile(fileContent)
    }
}
