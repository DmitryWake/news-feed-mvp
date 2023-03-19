package ru.newsfeedmvp.features.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.newsfeedmvp.database.daofacade.news.NewsDAOFacade
import ru.newsfeedmvp.database.daofacade.news.NewsDAOFacadeImpl
import ru.newsfeedmvp.features.datasource.rest.interactor.VkInteractor
import ru.newsfeedmvp.features.datasource.rss.model.base.NewsModel

class NewsFeedSourceWorker(
    private val interactors: List<NewsFeedInteractor>,
    private val newsDAOFacade: NewsDAOFacade
) {
    suspend fun loadNews() = withContext(Dispatchers.IO) {
        while (true) {
            interactors.forEach { interactor ->
                val resultList = kotlin.runCatching { interactor.getNewsFeedModel() }
                    .onFailure { it.printStackTrace() }.getOrNull()

                resultList?.let { processResult(it) }
            }

            delay(REPEAT_TIME_MS)
        }
    }

    private suspend fun processResult(resultList: List<NewsModel>) {
        resultList.forEach { model ->
            val id = model.id?.let { newsDAOFacade.entity(it) }?.id ?: newsDAOFacade.getBySourceUrl(model.sourceUrl)?.id
            if (id == null) {
                newsDAOFacade.addNewEntity(model)
            } else {
                newsDAOFacade.editEntity(model.copy(id = id))
            }
        }
    }

    companion object {
        /**
         * Создать core реализацию воркера
         */
        fun getInstance() = NewsFeedSourceWorker(BASE_ADAPTERS_MAP, BASE_NEWS_DAO)

        /**
         * Базовый ассоциативный массив адаптеров и соответствующих им адресов
         */
        val BASE_ADAPTERS_MAP = listOf<NewsFeedInteractor>(
            // Url("https://lenta.ru/rss/news") to LentaRuAdapter()
            VkInteractor()
        )

        /**
         * Core реализация [NewsDAOFacade]
         */
        val BASE_NEWS_DAO = NewsDAOFacadeImpl()

        private const val REPEAT_TIME_MS = 60 * 60 * 1000L
    }
}