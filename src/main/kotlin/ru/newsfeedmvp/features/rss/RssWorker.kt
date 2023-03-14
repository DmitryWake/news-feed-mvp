package ru.newsfeedmvp.features.rss

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.newsfeedmvp.core.client.getMainHttpClient
import ru.newsfeedmvp.database.daofacade.news.NewsDAOFacadeImpl
import ru.newsfeedmvp.database.daofacade.news.NewsDAOFacade
import ru.newsfeedmvp.features.rss.adapter.LentaRuAdapter
import ru.newsfeedmvp.features.rss.adapter.RSSFeedAdapter
import ru.newsfeedmvp.features.rss.model.base.NewsModel

class RssWorker(
    private val adapters: Map<Url, RSSFeedAdapter>,
    private val newsDAOFacade: NewsDAOFacade
) {
    suspend fun loadNews() = withContext(Dispatchers.IO) {
        while (true) {
            adapters.forEach { (url, adapter) ->
                val resultList = kotlin.runCatching { adapter.getNewsFeedModel(getMainHttpClient().get(url)) }
                    .onFailure { println(it) }.getOrNull()

                resultList?.let { processResult(it) }

                println(newsDAOFacade.allEntities().size)
            }

            delay(REPEAT_TIME_MS)
        }
    }

    private suspend fun processResult(resultList: List<NewsModel>) {
        resultList.forEach { model ->
            if (newsDAOFacade.getBySourceUrl(model.sourceUrl) == null) {
                newsDAOFacade.addNewEntity(model)
            }
        }
    }

    companion object {
        /**
         * Создать core реализацию воркера
         */
        fun getInstance() = RssWorker(BASE_ADAPTERS_MAP, BASE_NEWS_DAO)

        /**
         * Базовый ассоциативный массив адаптеров и соответствующих им адресов
         */
        val BASE_ADAPTERS_MAP = mapOf(
            Url("https://lenta.ru/rss/news") to LentaRuAdapter()
        )

        /**
         * Core реализация [NewsDAOFacade]
         */
        val BASE_NEWS_DAO = NewsDAOFacadeImpl()

        private const val REPEAT_TIME_MS = 30 * 60 * 1000L
    }
}