package com.example.apptracker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.linksTextView)
        val file = File(filesDir, "saved_links.txt")

        textView.text = if (file.exists() && file.readText().isNotBlank()) {
            file.readText()
        } else {
            "Пока нет сохранённых ссылок."
        }
    }
}
