package ru.newsfeedmvp.features.recommendation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.newsfeedmvp.database.daofacade.news.NewsDAOFacadeImpl
import ru.newsfeedmvp.database.daofacade.recommendation.RecommendationDAOFacadeImpl
import ru.newsfeedmvp.features.newsfeed.model.FeedItemModel
import ru.newsfeedmvp.features.newsuserscore.NewsUserScoreRepository

class RecommendationRepository {

    private val recommendationDAOFacade = RecommendationDAOFacadeImpl.instance
    private val newsDAOFacade = NewsDAOFacadeImpl.instance
    private val newsUserScoreRepository = NewsUserScoreRepository.instance

    suspend fun getRecommendationNewsFeedPaging(userId: String, limit: Int, offset: Long): List<FeedItemModel> =
        withContext(Dispatchers.IO) {
            val recommended = recommendationDAOFacade.getByUser(userId, limit, offset)

            recommended.mapNotNull { newsDAOFacade.entity(it.newsId) }.map {
                FeedItemModel(
                    id = requireNotNull(it.id),
                    newsBody = it.newsBody,
                    imageUrl = it.imageUrl,
                    sourceUrl = it.sourceUrl,
                    date = it.date,
                    trustIndex = newsUserScoreRepository.getNewsTrustIndex(requireNotNull(it.id)),
                    currentUserScore = newsUserScoreRepository.getScoresByUser(userId).userScores[it.id]
                )
            }
        }

    companion object {
        val instance by lazy { RecommendationRepository() }
    }
}