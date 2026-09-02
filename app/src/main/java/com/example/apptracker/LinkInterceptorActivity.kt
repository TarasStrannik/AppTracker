package com.example.apptracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.File

// Работает как "браузер по умолчанию": ловит вообще все ссылки http/https.
// Если это ссылка на Play Market - спрашивает "сохранить?".
// Если это любая другая ссылка (обычный сайт) - молча пересылает в настоящий браузер.
class LinkInterceptorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri: Uri? = intent?.data
        if (uri == null) {
            finish()
            return
        }

        if (isPlayStoreLink(uri)) {
            showSaveDialog(uri)
        } else {
            openInBrowser(uri)
            finish()
        }
    }

    private fun isPlayStoreLink(uri: Uri): Boolean {
        val host = uri.host ?: ""
        return uri.scheme == "market" ||
            host == "play.google.com" ||
            host == "market.android.com"
    }

    private fun showSaveDialog(uri: Uri) {
        AlertDialog.Builder(this)
            .setTitle("Сохранить ссылку?")
            .setMessage(uri.toString())
            .setCancelable(false)
            .setPositiveButton("Да") { _, _ ->
                saveLink(uri.toString())
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Нет") { _, _ ->
                openInPlayStore(uri)
                finish()
            }
            .show()
    }

    private fun saveLink(link: String) {
        val file = File(filesDir, "saved_links.txt")
        file.appendText("$link\n")
    }

    private fun openInPlayStore(uri: Uri) {
        val playStoreIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.android.vending")
        }
        try {
            startActivity(playStoreIntent)
        } catch (e: Exception) {
            openInBrowser(uri)
        }
    }

    private fun openInBrowser(uri: Uri) {
        val chromeIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.android.chrome")
        }
        try {
            startActivity(chromeIntent)
            return
        } catch (e: Exception) {
            // Chrome не найден - ищем любой другой браузер, кроме себя самих
        }

        val genericIntent = Intent(Intent.ACTION_VIEW, uri)
        val candidates = packageManager.queryIntentActivities(genericIntent, 0)
        val other = candidates.firstOrNull { it.activityInfo.packageName != packageName }

        if (other != null) {
            val explicitIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                setClassName(other.activityInfo.packageName, other.activityInfo.name)
            }
            startActivity(explicitIntent)
        } else {
            Toast.makeText(this, "Не найден браузер для этой ссылки", Toast.LENGTH_SHORT).show()
        }
    }
}
