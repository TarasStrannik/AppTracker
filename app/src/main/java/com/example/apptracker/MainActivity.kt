package com.example.apptracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var linksFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linksFile = File(filesDir, "saved_links.txt")

        val textView = findViewById<TextView>(R.id.linksTextView)
        textView.text = if (linksFile.exists() && linksFile.readText().isNotBlank()) {
            linksFile.readText()
        } else {
            "Пока нет сохранённых ссылок."
        }

        findViewById<Button>(R.id.shareButton).setOnClickListener {
            shareLinksFile()
        }
    }

    private fun shareLinksFile() {
        if (!linksFile.exists()) {
            linksFile.createNewFile()
        }

        val uri: Uri = FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider",
            linksFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Отправить файл со ссылками"))
    }
}
