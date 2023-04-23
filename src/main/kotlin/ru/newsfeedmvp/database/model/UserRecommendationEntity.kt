package ru.newsfeedmvp.database.model

data class UserRecommendationEntity(
    val id: Int? = null,
    val userId: String,
    val scoresCount: UInt,
    val successfulScoresCount: UInt
)
