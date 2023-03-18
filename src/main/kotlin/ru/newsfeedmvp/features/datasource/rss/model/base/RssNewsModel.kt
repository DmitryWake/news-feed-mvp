package ru.newsfeedmvp.features.datasource.rss.model.base

data class NewsModel(
    val id: Int? = null,
    val newsBody: String,
    val imageUrl: String?,
    val sourceUrl: String
)
