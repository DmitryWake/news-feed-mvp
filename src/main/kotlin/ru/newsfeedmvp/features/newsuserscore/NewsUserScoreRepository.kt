package ru.newsfeedmvp.features.newsuserscore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.newsfeedmvp.core.model.TrustIndexModel
import ru.newsfeedmvp.database.daofacade.trustusernews.TrustUserNewsDAOFacade
import ru.newsfeedmvp.database.daofacade.trustusernews.TrustUserNewsDAOFacadeImpl
import ru.newsfeedmvp.database.model.TrustUserNewsEntity
import ru.newsfeedmvp.features.newsuserscore.model.NewsUserScoreModel

class NewsUserScoreRepository {

    private val facade: TrustUserNewsDAOFacade = TrustUserNewsDAOFacadeImpl.instance

    suspend fun getScoresByUser(userId: String): NewsUserScoreModel = withContext(Dispatchers.IO) {
        val scores = facade.getEntitiesByUser(userId).associate { it.newsId to it.trustValue }

        NewsUserScoreModel(
            userId = userId,
            userScores = scores
        )
    }

    suspend fun getNewsTrustIndex(newsId: Int): TrustIndexModel = withContext(Dispatchers.IO) {
        val scores = facade.getEntitiesByNews(newsId).map { it.trustValue }
        val trustCount = scores.count { it }
        val distrustCount = scores.size - trustCount

        TrustIndexModel(
            totalCount = scores.size,
            trustCount = trustCount,
            distrustCount = distrustCount
        )
    }

    suspend fun getNewsTrustIndexes(): Map<Int, TrustIndexModel> = withContext(Dispatchers.IO) {
        facade.allEntities().groupBy { it.newsId }.mapValues {mapEntry ->
            val trustCount = mapEntry.value.count { it.trustValue }
            TrustIndexModel(
                totalCount = mapEntry.value.size,
                trustCount = trustCount,
                distrustCount = mapEntry.value.size - trustCount
            )
        }
    }

    suspend fun setScoreByUser(userId: String, newsId: Int, score: Boolean): Boolean = withContext(Dispatchers.IO) {
        facade.addNewEntity(
            TrustUserNewsEntity(
                userId = userId,
                newsId = newsId,
                trustValue = score
            )
        ) != null
    }

    companion object {
        val instance by lazy { NewsUserScoreRepository() }
    }
}