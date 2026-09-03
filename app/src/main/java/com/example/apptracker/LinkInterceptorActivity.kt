package com.example.apptracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

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
        val cleanedLink = LinkUtils.cleanLink(uri.toString())

        AlertDialog.Builder(this)
            .setTitle("Сохранить ссылку?")
            .setMessage(cleanedLink)
            .setCancelable(false)
            .setPositiveButton("Да") { _, _ ->
                LinkUtils.saveLink(this, cleanedLink)
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Нет") { _, _ ->
                openInPlayStore(uri)
                finish()
            }
            .show()
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
