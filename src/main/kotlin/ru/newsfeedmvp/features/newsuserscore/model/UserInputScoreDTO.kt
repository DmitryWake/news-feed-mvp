package ru.newsfeedmvp.features.newsuserscore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInputScoreDTO(
    val newsId: Int,
    val score: Boolean
)