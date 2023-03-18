package ru.newsfeedmvp.features.datasource.rss.model.base

data class NewsModel(
    val id: Int? = null,
    val newsBody: String,
    val imageUrl: String? = null,
    val sourceUrl: String,
    val date: Long,
    val viewsCount: Int? = null,
    val likesCount: Int? = null,
    val repostsCount: Int? = null,
    val commentsCount: Int? = null
)
