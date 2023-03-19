package ru.newsfeedmvp.features.nlp.tonepredictor

import ru.newsfeedmvp.features.nlp.tonepredictor.model.ReactionType

interface TonePredictor {
    suspend fun checkTone(data: List<String>): ReactionType
}