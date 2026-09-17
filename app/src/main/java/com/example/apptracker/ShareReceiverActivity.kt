package com.example.apptracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ShareReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedText = if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }

        if (sharedText.isNullOrBlank()) {
            finish()
            return
        }

        val cleanedLink = LinkUtils.cleanLink(sharedText)

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
                openInPlayStore(cleanedLink)
                finish()
            }
            .show()
    }

    private fun openInPlayStore(link: String) {
        val uri = Uri.parse(link)
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
