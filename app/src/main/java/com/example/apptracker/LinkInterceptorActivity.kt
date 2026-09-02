package com.example.apptracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.File

// Ловит клик по ссылке Play Market (play.google.com, market.android.com, market://)
// до того, как откроется сам Play Market, и спрашивает - сохранить или пропустить.
class LinkInterceptorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri: Uri? = intent?.data

        if (uri == null) {
            finish()
            return
        }

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
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
}
