package ru.newsfeedmvp.core.application

import io.ktor.server.application.*
import kotlinx.coroutines.launch
import ru.newsfeedmvp.features.datasource.NewsFeedSourceWorker
import ru.newsfeedmvp.features.recommendation.RecommendationWorker

fun Application.runStartingTasks() {
    launch { NewsFeedSourceWorker.getInstance().loadNews() }
    launch { RecommendationWorker.instance.startRecommendationJob() }
}