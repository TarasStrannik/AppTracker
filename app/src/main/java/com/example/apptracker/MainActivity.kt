package com.example.apptracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var linksFile: File
    private lateinit var textView: TextView

    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) loadSortLinksFile(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linksFile = File(filesDir, "saved_links.txt")
        textView = findViewById(R.id.linksTextView)

        findViewById<Button>(R.id.shareButton).setOnClickListener {
            shareLinksFile()
        }

        findViewById<Button>(R.id.loadButton).setOnClickListener {
            pickFileLauncher.launch("text/plain")
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        textView.text = if (linksFile.exists() && linksFile.readText().isNotBlank()) {
            linksFile.readText()
        } else {
            "Пока нет сохранённых ссылок."
        }
    }

    private fun loadSortLinksFile(uri: Uri) {
        try {
            contentResolver.openInputStream(uri)?.use { input ->
                val content = input.bufferedReader().readText()
                File(filesDir, "sort_links.txt").writeText(content)
                val count = content.lines().count { it.isNotBlank() }
                Toast.makeText(this, "Загружено записей: $count", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Не удалось загрузить файл", Toast.LENGTH_SHORT).show()
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
