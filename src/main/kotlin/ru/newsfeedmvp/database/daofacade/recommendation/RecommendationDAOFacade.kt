package ru.newsfeedmvp.database.daofacade.recommendation

import ru.newsfeedmvp.database.daofacade.DAOFacade
import ru.newsfeedmvp.database.model.RecommendationEntity

interface RecommendationDAOFacade: DAOFacade<RecommendationEntity, Int> {
    suspend fun getByUser(userId: String, limit: Int, offset: Long): List<RecommendationEntity>
    suspend fun addEntities(data: List<RecommendationEntity>): List<RecommendationEntity>
    suspend fun deleteAll()
}