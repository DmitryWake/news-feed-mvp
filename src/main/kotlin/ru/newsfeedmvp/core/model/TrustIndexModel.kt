package ru.newsfeedmvp.core.model

import kotlinx.serialization.Serializable

@Serializable
data class TrustIndexModel(
    val totalCount: Int,
    val trustCount: Int,
    val distrustCount: Int
)
