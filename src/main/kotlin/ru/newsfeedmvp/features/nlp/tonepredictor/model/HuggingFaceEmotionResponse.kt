package ru.newsfeedmvp.features.nlp.tonepredictor.model

import kotlinx.serialization.Serializable

@Serializable
data class HuggingFaceEmotionResponse(
    val label: String,
    val score: Double
) {
    val enumLabel: ReactionType?
        get() = kotlin.runCatching { ReactionType.valueOf(label.uppercase()) }.getOrNull()
}
