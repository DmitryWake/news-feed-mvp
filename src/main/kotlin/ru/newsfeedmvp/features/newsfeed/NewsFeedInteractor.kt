package ru.newsfeedmvp.features.newsfeed

import ru.newsfeedmvp.features.newsfeed.model.FeedItemModel
import ru.newsfeedmvp.features.newsuserscore.NewsUserScoreRepository

class NewsFeedInteractor {

    private val newsFeedRepository = NewsFeedRepository.instance
    private val newsUserScoreRepository = NewsUserScoreRepository.instance

    suspend fun getNewsFeed(userId: String, count: Int, offset: Long): List<FeedItemModel> =
        newsFeedRepository.getNewsFeedPaging(count, offset).map {
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

    suspend fun getLastNewsIds(): List<Int> =
        newsFeedRepository.getNewsFeedPaging(10000, 0).mapNotNull { it.id }

    companion object {
        val instance by lazy { NewsFeedInteractor() }
    }
}