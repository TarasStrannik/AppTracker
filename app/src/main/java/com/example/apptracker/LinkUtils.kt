package com.example.apptracker

import android.content.Context
import java.io.File

object LinkUtils {

    fun cleanLink(raw: String): String {
        return raw.substringBefore("&").trim()
    }

    fun saveLink(context: Context, link: String) {
        val file = File(context.filesDir, "saved_links.txt")
        file.appendText("$link\n")
    }
}
