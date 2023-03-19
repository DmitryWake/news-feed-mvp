package ru.newsfeedmvp.features.datasource.rest.interactor

import ru.newsfeedmvp.features.datasource.NewsFeedInteractor
import ru.newsfeedmvp.features.datasource.rest.repository.VkRepository
import ru.newsfeedmvp.features.datasource.rss.model.base.NewsModel
import ru.newsfeedmvp.features.nlp.tonepredictor.RestTonePredictor
import ru.newsfeedmvp.features.nlp.tonepredictor.model.ReactionType
import java.util.*

class VkInteractor : NewsFeedInteractor {

    private val vkRepository = VkRepository()
    private val tonePredictor = RestTonePredictor()

    override suspend fun getNewsFeedModel(): List<NewsModel> {
        return vkRepository.getNewsFeed(LENTA_RU_VK_ID).response?.items?.map { item ->
            NewsModel(
                newsBody = item.text.orEmpty(),
                imageUrl = item.attachments.firstOrNull()?.photo?.sizes?.firstOrNull()?.url,
                sourceUrl = item.id.toString(),
                date = item.date ?: Date().time,
                viewsCount = item.views?.count,
                likesCount = item.likes?.count,
                repostsCount = item.reposts?.count,
                commentsCount = item.comments?.count,
                reactionType = calculateReaction(item.id)
            )
        } ?: listOf()
    }

    private suspend fun calculateReaction(postId: Int): ReactionType? =
        vkRepository.getNewsComments(LENTA_RU_VK_ID, postId).response?.items?.mapNotNull { it.text }?.let {
            tonePredictor.checkTone(it)
        }

    companion object {
        private const val LENTA_RU_VK_ID = -67991642
    }
}