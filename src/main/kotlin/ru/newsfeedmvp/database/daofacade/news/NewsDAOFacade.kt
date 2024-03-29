package ru.newsfeedmvp.database.daofacade.news

import ru.newsfeedmvp.database.daofacade.DAOFacade
import ru.newsfeedmvp.features.datasource.rss.model.base.NewsModel

interface NewsDAOFacade: DAOFacade<NewsModel, Int> {
    suspend fun getBySourceUrl(sourceUrl: String): NewsModel?
    suspend fun getPagingFeed(count: Int, offset: Long): List<NewsModel>
}