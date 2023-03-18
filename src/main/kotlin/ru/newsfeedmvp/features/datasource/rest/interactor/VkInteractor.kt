package ru.newsfeedmvp.features.datasource.rest.interactor

import ru.newsfeedmvp.features.datasource.NewsFeedInteractor
import ru.newsfeedmvp.features.datasource.rest.repository.VkRepository
import ru.newsfeedmvp.features.datasource.rss.model.base.NewsModel

class VkInteractor: NewsFeedInteractor {

    private val vkRepository = VkRepository()

    override suspend fun getNewsFeedModel(): List<NewsModel> {
        return vkRepository.getNewsFeed(LENTA_RU_VK_ID).response?.items?.map { item ->
            NewsModel(
                newsBody = item.text,
                imageUrl = item.attachments.firstOrNull()?.photo?.sizes?.firstOrNull()?.url,
                sourceUrl = item.id.toString()
            )
        } ?: listOf()
    }

    companion object {
        private const val LENTA_RU_VK_ID = "-67991642"
    }
}