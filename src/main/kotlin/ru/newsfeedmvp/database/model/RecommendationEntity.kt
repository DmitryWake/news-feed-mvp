package ru.newsfeedmvp.database.model

data class RecommendationEntity(
    val id: Int? = null,
    val userId: String,
    val newsId: Int,
    val score: Double
)
