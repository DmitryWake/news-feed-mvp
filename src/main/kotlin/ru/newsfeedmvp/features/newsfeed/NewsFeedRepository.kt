package ru.newsfeedmvp.features.newsfeed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.newsfeedmvp.database.daofacade.news.NewsDAOFacade
import ru.newsfeedmvp.database.daofacade.news.NewsDAOFacadeImpl

class NewsFeedRepository {

    private val newsDao: NewsDAOFacade = NewsDAOFacadeImpl.instance

    suspend fun getNewsFeedPaging(count: Int, offset: Long = 0) = withContext(Dispatchers.IO) {
        newsDao.getPagingFeed(count, offset)
    }

    companion object {
        val instance by lazy { NewsFeedRepository() }
    }
}