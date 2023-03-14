package ru.newsfeedmvp.core.application

import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.newsfeedmvp.features.rss.RssWorker

fun Application.runStartingTasks() {
    launch {
        // Pause working with rss
        // RssWorker.getInstance().loadNews()
    }
}