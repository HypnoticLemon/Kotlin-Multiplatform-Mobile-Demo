package com.vikrant.mykmmexampleapplication.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vikrant.mykmmexampleapplication.fileSystem.FileUtils

class FileReadActivity : AppCompatActivity() {
    private lateinit var tvLabel: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_read)

        tvLabel = findViewById(R.id.tvLabel)

        tvLabel.text = FileUtils().readFile()

    }
}