package ru.newsfeedmvp.features.rss.model.base

data class NewsModel(
    val id: Int? = null,
    val title: String,
    val description: String,
    val imageUrl: String,
    val sourceUrl: String
)
