package com.vikrant.mykmmexampleapplication.fileSystem

import android.util.Log
import com.suparnatural.core.fs.ContentEncoding
import com.suparnatural.core.fs.FileSystem

class FileUtils() {
    fun readFile(): String? {
        var filePath = FileSystem.contentsDirectory.absolutePath?.byAppending("test.txt")?.component
        return FileSystem.readFile(filePath.toString(), ContentEncoding.Utf8)
    }

    fun writeFile(fileContent:String): Boolean {
        var filePath = FileSystem.contentsDirectory.absolutePath?.byAppending("test.txt")?.component
        Log.e("PostSDK", "writeFile: $filePath")
        return FileSystem.appendFile(filePath.toString(), fileContent + "\n",true, ContentEncoding.Utf8)
    }
}