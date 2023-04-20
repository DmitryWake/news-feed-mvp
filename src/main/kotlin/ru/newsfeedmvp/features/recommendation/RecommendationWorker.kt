package ru.newsfeedmvp.features.recommendation

import kotlinx.coroutines.*
import ru.newsfeedmvp.database.daofacade.recommendation.RecommendationDAOFacadeImpl
import ru.newsfeedmvp.database.model.RecommendationEntity
import ru.newsfeedmvp.features.newsfeed.NewsFeedInteractor
import ru.newsfeedmvp.features.newsuserscore.NewsUserScoreRepository
import ru.newsfeedmvp.features.user.UserInteractor

class RecommendationWorker {

    private val userInteractor = UserInteractor.instance
    private val newsFeedInteractor = NewsFeedInteractor.instance
    private val newsUserScoreRepository = NewsUserScoreRepository.instance
    private val recommendationDAOFacadeImpl = RecommendationDAOFacadeImpl.instance

    suspend fun startRecommendationJob() = withContext(Dispatchers.IO) {
        while (true) {
            val newsIds = newsFeedInteractor.getLastNewsIds()
            val usersScores = userInteractor.getUsers().also { println(it.size) }.map { user ->
                newsUserScoreRepository.getScoresByUser(user.id).let { scores ->
                    scores.copy(userScores = scores.userScores.filter { newsIds.contains(it.key) })
                }
            }.associate { Pair(it.userId, it.userScores) }

            val cosSimilarity = calculateSimilarity(usersScores).also { println(it.values.sumOf { it.size }) }
            val nearestSimilarity = calculateNearests(cosSimilarity).also { println(it.values) }

            val result = nearestSimilarity.mapValues { mapEntry ->
                calculateRecommendations(
                    mapEntry.value,
                    newsIds.filter { !usersScores[mapEntry.key]!!.containsKey(it) },
                    usersScores
                ).also { println(it) }
            }.mapValues { mapEntry ->
                mapEntry.value.map { RecommendationEntity(userId = mapEntry.key, newsId = it.key, score = it.value) }
            }.values.filter { it.isNotEmpty() }

            recommendationDAOFacadeImpl.deleteAll()
            result.forEach { recommendationDAOFacadeImpl.addEntities(it) }

            delay(DELAY)
        }
    }

    /**
     * @param userScores - содержит в себе ассоциативный массив вида: Map<UserId, Map<NewsId, Score by UserId>>
     * @return ассоциативный массив вида Map<UserId, Map<AnotherUserId, Similarity between them>>
     */
    private suspend fun calculateSimilarity(userScores: Map<String, Map<Int, Boolean>>): MutableMap<String, MutableMap<String, Double>> =
        withContext(Dispatchers.Default) {
            val resultMap = mutableMapOf<String, MutableMap<String, Deferred<Double>>>()

            userScores.forEach { chosenUser ->
                userScores.forEach { otherUser ->
                    if (chosenUser != otherUser &&
                        resultMap[chosenUser.key]?.get(otherUser.key) == null &&
                        resultMap[otherUser.key]?.get(chosenUser.key) == null
                    ) {
                        val cosSimDeffer = async { calculateCosSimilarity(chosenUser.value, otherUser.value) }

                        if (resultMap.containsKey(chosenUser.key)) {
                            resultMap[chosenUser.key]!![otherUser.key] = cosSimDeffer
                        } else {
                            resultMap[chosenUser.key] = mutableMapOf(otherUser.key to cosSimDeffer)
                        }
                    }
                }
            }

            resultMap.mapValues { resValues ->
                resValues.value.mapValues {
                    it.value.await()
                }.toMutableMap()
            }.toMutableMap()
        }

    private fun calculateCosSimilarity(
        firstUserScores: Map<Int, Boolean>,
        secondUserScores: Map<Int, Boolean>
    ): Double {
        val firstVector = firstUserScores.toSortedMap()
            .filter { secondUserScores.containsKey(it.key) }
            .map { it.value }

        val secondVector = secondUserScores.toSortedMap()
            .filter { firstUserScores.containsKey(it.key) }
            .map { it.value }

        var dotValue = 0

        firstVector.forEachIndexed { index, value ->
            val secondValue = secondVector[index]
            dotValue += if (value == secondValue) 1 else -1
        }

        return if (dotValue == 0) 0.0 else (dotValue / firstVector.size.toDouble())
    }

    private fun calculateNearests(data: MutableMap<String, MutableMap<String, Double>>): MutableMap<String, MutableMap<String, Double>> =
        data.mapValues { mapEntry ->
            mapEntry.value.toList().sortedByDescending { it.second }.take(NEAREST_COUNT).associate { it }.toMutableMap()
        }.toMutableMap()

    private suspend fun calculateRecommendations(
        userSimilarity: Map<String, Double>,
        uncheckedNews: List<Int>,
        userScores: Map<String, Map<Int, Boolean>>
    ): Map<Int, Double> = withContext(Dispatchers.Default) {
        val result = mutableMapOf<Int, Double>()

        uncheckedNews.forEach { newsId ->
            val simUserScores = userSimilarity.filter { userScores[it.key]?.containsKey(newsId) == true }

            if (simUserScores.isNotEmpty()) {
                (simUserScores.values.sum() / simUserScores.size).let {
                    if (it > 0) result[newsId] = it
                }
            }
        }

        result.toList().sortedByDescending { it.second }.associate { it }
    }

    companion object {
        val instance by lazy { RecommendationWorker() }

        /**
         * Delay for job 8 hours
         */
        private const val DELAY = 8 * 60 * 60 * 1000L

        private const val NEAREST_COUNT = 5
    }
}