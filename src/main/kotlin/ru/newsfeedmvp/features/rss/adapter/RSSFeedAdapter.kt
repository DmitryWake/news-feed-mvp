package ru.newsfeedmvp.features.rss.adapter

import io.ktor.client.statement.*
import ru.newsfeedmvp.features.rss.model.base.NewsModel

interface RSSFeedAdapter {
    suspend fun getNewsFeedModel(response: HttpResponse): List<NewsModel>
}