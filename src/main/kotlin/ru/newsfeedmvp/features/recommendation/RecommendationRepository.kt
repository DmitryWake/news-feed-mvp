package ru.newsfeedmvp.features.recommendation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.newsfeedmvp.database.daofacade.news.NewsDAOFacadeImpl
import ru.newsfeedmvp.database.daofacade.recommendation.RecommendationDAOFacadeImpl
import ru.newsfeedmvp.database.daofacade.recommendation.UserRecommendationDAOFacadeImpl
import ru.newsfeedmvp.database.model.UserRecommendationEntity
import ru.newsfeedmvp.features.newsfeed.model.FeedItemModel
import ru.newsfeedmvp.features.newsuserscore.NewsUserScoreRepository

class RecommendationRepository {

    private val recommendationDAOFacade = RecommendationDAOFacadeImpl.instance
    private val userRecommendationDAOFacade = UserRecommendationDAOFacadeImpl.instance
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

    suspend fun setRecommendationScore(userId: String, score: Boolean): Boolean = withContext(Dispatchers.IO) {
        val currentScores = userRecommendationDAOFacade.getByUser(userId)

        if (currentScores == null) {
            userRecommendationDAOFacade.addNewEntity(
                UserRecommendationEntity(
                    scoresCount = 1u,
                    userId = userId,
                    successfulScoresCount = if (score) 1u else 0u
                )
            ) != null
        } else {
            userRecommendationDAOFacade.editEntity(
                currentScores.copy(
                    scoresCount = currentScores.scoresCount + 1u,
                    successfulScoresCount = if (score) currentScores.successfulScoresCount + 1u else currentScores.successfulScoresCount
                )
            )
        }
    }

    companion object {
        val instance by lazy { RecommendationRepository() }
    }
}