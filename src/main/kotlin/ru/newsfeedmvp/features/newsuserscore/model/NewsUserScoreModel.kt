package ru.newsfeedmvp.features.newsuserscore.model

/**
 * @param userId - айди юзера, который выставляет оценки
 * @param userScores - оценки юзера, как Map: newsId to Trust(true)/Distrust(false)
 */
data class NewsUserScoreModel(
    val userId: String,
    val userScores: Map<Int, Boolean>
)
