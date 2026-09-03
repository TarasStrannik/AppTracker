package com.example.apptracker

import android.content.Context
import java.io.File

object LinkUtils {

    fun cleanLink(raw: String): String {
        return raw.substringBefore("&").trim()
    }

    fun extractId(link: String): String {
        return link.substringAfterLast("=").trim()
    }

    fun saveLink(context: Context, link: String) {
        val file = File(context.filesDir, "saved_links.txt")
        file.appendText("$link\n")
    }

    fun isKnownId(context: Context, id: String): Boolean {
        val file = File(context.filesDir, "sort_links.txt")
        if (!file.exists() || id.isBlank()) return false
        return file.readLines().any { it.trim() == id }
    }
}
