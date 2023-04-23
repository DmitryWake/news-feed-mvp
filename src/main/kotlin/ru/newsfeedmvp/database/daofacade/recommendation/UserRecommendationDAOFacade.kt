package ru.newsfeedmvp.database.daofacade.recommendation

import ru.newsfeedmvp.database.daofacade.DAOFacade
import ru.newsfeedmvp.database.model.UserRecommendationEntity

interface UserRecommendationDAOFacade: DAOFacade<UserRecommendationEntity, Int> {
    suspend fun getByUser(userId: String): UserRecommendationEntity?
}