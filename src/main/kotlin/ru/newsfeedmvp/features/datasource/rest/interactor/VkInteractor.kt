package ru.newsfeedmvp.features.datasource.rest.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.newsfeedmvp.features.datasource.NewsFeedInteractor
import ru.newsfeedmvp.features.datasource.rest.model.VkFeedResponse
import ru.newsfeedmvp.features.datasource.rest.repository.VkRepository
import ru.newsfeedmvp.features.datasource.rss.model.base.NewsModel
import ru.newsfeedmvp.features.nlp.tonepredictor.RestTonePredictor
import ru.newsfeedmvp.features.nlp.tonepredictor.model.ReactionType
import java.util.*

class VkInteractor : NewsFeedInteractor {

    private val vkRepository = VkRepository()
    private val tonePredictor = RestTonePredictor()

    // FIXME Рефакторинг
    override suspend fun getNewsFeedModel(): List<NewsModel> = withContext(Dispatchers.IO) {
        val lentaFeed = async {
            vkRepository.getNewsFeed(LENTA_RU_VK_ID).response?.items?.mapNotNull { item ->
                item.text?.let {
                    item.convertToNewsModel()
                }
            } ?: listOf()
        }

        val fontankaFeed = async {
            vkRepository.getNewsFeed(FONTANKA_VK_ID).response?.items?.mapNotNull { item ->
                item.text?.let {
                    item.convertToNewsModel()
                }
            } ?: listOf()
        }

        val kb = async {
            vkRepository.getNewsFeed(KB_VK_ID).response?.items?.mapNotNull { item ->
                item.text?.let {
                    item.convertToNewsModel()
                }
            } ?: listOf()
        }

        val importantNews = async {
            vkRepository.getNewsFeed(IMPORTANT_NEWS_VK_ID).response?.items?.mapNotNull { item ->
                item.text?.let {
                    item.convertToNewsModel()
                }
            } ?: listOf()
        }

        val fastNews = async {
            vkRepository.getNewsFeed(FAST_NEWS_VK_ID).response?.items?.mapNotNull { item ->
                item.text?.let {
                    item.convertToNewsModel()
                }
            } ?: listOf()
        }

        lentaFeed.await().plus(fontankaFeed.await()).plus(kb.await()).plus(importantNews.await()).plus(fastNews.await())
    }

    private suspend fun VkFeedResponse.Items.convertToNewsModel() = NewsModel(
        newsBody = text.orEmpty(),
        imageUrl = attachments.firstOrNull {
            !it.photo?.sizes.isNullOrEmpty()
        }?.photo?.sizes?.maxBy { it.height + it.width }?.url,
        sourceUrl = id.toString(),
        date = date ?: Date().time,
        viewsCount = views?.count,
        likesCount = likes?.count,
        repostsCount = reposts?.count,
        commentsCount = comments?.count,
        reactionType = calculateReaction(id)
    )

    private suspend fun calculateReaction(postId: Int): ReactionType? = kotlin.runCatching {
        vkRepository.getNewsComments(LENTA_RU_VK_ID, postId).response?.items?.mapNotNull { it.text }?.let {
            tonePredictor.checkTone(it)
        }
    }.getOrNull()


    companion object {
        private const val LENTA_RU_VK_ID = -67991642
        private const val FONTANKA_VK_ID = -18901857
        private const val KB_VK_ID = -67580761
        private const val IMPORTANT_NEWS_VK_ID = -153552070
        private const val FAST_NEWS_VK_ID = -188318774
    }
}