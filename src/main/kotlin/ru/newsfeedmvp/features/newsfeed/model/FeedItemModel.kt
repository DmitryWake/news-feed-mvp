package ru.newsfeedmvp.features.newsfeed.model

import kotlinx.serialization.Serializable
import ru.newsfeedmvp.core.model.TrustIndexModel

@Serializable
data class FeedItemModel(
    val id: Int,
    val newsBody: String,
    val imageUrl: String? = null,
    val sourceUrl: String,
    val date: Long,
    val trustIndex: TrustIndexModel,
    val currentUserScore: Boolean? = null
)
