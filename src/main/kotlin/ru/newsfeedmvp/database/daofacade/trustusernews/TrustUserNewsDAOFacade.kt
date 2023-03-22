package ru.newsfeedmvp.database.daofacade.trustusernews

import ru.newsfeedmvp.database.daofacade.DAOFacade
import ru.newsfeedmvp.database.model.TrustUserNewsEntity

interface TrustUserNewsDAOFacade: DAOFacade<TrustUserNewsEntity, Int> {
    suspend fun getEntitiesByUser(userId: String): List<TrustUserNewsEntity>
    suspend fun getEntitiesByNews(newsId: Int): List<TrustUserNewsEntity>
}