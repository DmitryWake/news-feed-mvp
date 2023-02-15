package ru.newsfeedmvp.features.rss

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.newsfeedmvp.core.client.getMainHttpClient
import ru.newsfeedmvp.features.rss.adapter.LentaRuAdapter
import ru.newsfeedmvp.features.rss.adapter.RSSFeedAdapter

class RssWorker(
    private val adapters: Map<Url, RSSFeedAdapter>
) {
    suspend fun loadNews() = withContext(Dispatchers.IO) {
        while (true) {
            adapters.forEach { (url, adapter) ->
                val rssFeed = adapter.getNewsFeedModel(getMainHttpClient().get(url))
                println(url to rssFeed.size)
            }

            delay(REPEAT_TIME_MS)
        }
    }

    companion object {
        fun getInstance() = RssWorker(BASE_ADAPTERS_MAP)

        private val BASE_ADAPTERS_MAP = mapOf(Url("https://lenta.ru/rss/news") to LentaRuAdapter())

        private const val REPEAT_TIME_MS = 2 * 60 * 1000L
    }
}