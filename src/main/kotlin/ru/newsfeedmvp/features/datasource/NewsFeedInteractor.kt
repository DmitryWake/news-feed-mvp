package ru.newsfeedmvp.features.datasource

import io.ktor.client.statement.*
import ru.newsfeedmvp.features.datasource.rss.model.base.NewsModel

interface NewsFeedInteractor {
    suspend fun getNewsFeedModel(): List<NewsModel>
}