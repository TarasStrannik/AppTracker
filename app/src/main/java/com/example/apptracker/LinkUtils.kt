package com.example.apptracker

import android.content.Context
import java.io.File

// Общая логика для ShareReceiverActivity и LinkInterceptorActivity
object LinkUtils {

    // Обрезаем всё после "&" - там едет реферальный мусор рекламных сетей,
    // нам нужна только чистая ссылка на страницу приложения.
    fun cleanLink(raw: String): String {
        return raw.substringBefore("&").trim()
    }

    fun saveLink(context: Context, link: String) {
        val file = File(context.filesDir, "saved_links.txt")
        file.appendText("$link\n")
    }
}
