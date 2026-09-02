package com.example.apptracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Экран-невидимка: ловит ссылку из "Поделиться", парсит package id и пока
// просто показывает Toast. Здесь позже появится сохранение в базу данных.
class ShareReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedText = if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }

        val message = if (sharedText != null) {
            val packageId = extractPackageId(sharedText)
            if (packageId != null) "Получено: $packageId" else "Ссылка получена, но это не Play Market:\n$sharedText"
        } else {
            "Ссылка не получена"
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }

    private fun extractPackageId(text: String): String? {
        val regex = Regex("""id=([a-zA-Z0-9_.]+)""")
        return regex.find(text)?.groupValues?.get(1)
    }
}
