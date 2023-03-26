package ru.newsfeedmvp.features.newsfeed.model

import kotlinx.serialization.Serializable

@Serializable
data class FeedInputParamsDTO(
    val count: Int,
    val offset: Long
)
