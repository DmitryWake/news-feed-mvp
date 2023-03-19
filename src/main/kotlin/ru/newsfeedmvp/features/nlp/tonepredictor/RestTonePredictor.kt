package ru.newsfeedmvp.features.nlp.tonepredictor

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import ru.newsfeedmvp.core.client.getMainHttpClient
import ru.newsfeedmvp.features.nlp.tonepredictor.model.HuggingFaceEmotionResponse
import ru.newsfeedmvp.features.nlp.tonepredictor.model.ReactionType
import java.io.File

class RestTonePredictor: TonePredictor {

    private var isInit: Boolean = false
    private val serviceToken = File(SERVICE_TOKEN_FILE_PATH).readLines().first()

    override suspend fun checkTone(data: List<String>): ReactionType {
        withTimeout(INIT_TIMEOUT) {
            while (isInit.not()) {
                isInit = kotlin.runCatching {
                    getData(listOf("test"))
                }.isSuccess
                if (isInit.not()) {
                    delay(INIT_TRY_DELAY)
                }
            }
        }

        val response = kotlin.runCatching { getData(data) }.getOrNull()

        return if (response.isNullOrEmpty()) {
             ReactionType.NO_EMOTION
        } else {
            response.map { it.maxBy { it.score } }
                .groupBy { it.enumLabel }
                .maxBy { it.value.size }.key ?: ReactionType.NO_EMOTION
        }
    }

    private suspend fun getData(data: List<String>): List<List<HuggingFaceEmotionResponse>> =
        getMainHttpClient().post(TRANSFORMER_URL) {
            header("Authorization", "Bearer $serviceToken")
            contentType(ContentType.parse("application/json"))
            setBody(data)
        }.also { println(it.bodyAsText()) }.body()

    companion object {
        private const val TRANSFORMER_URL = "https://api-inference.huggingface.co/models/cointegrated/rubert-tiny2-cedr-emotion-detection"
        private const val INIT_TIMEOUT = 120 * 1000L
        private const val INIT_TRY_DELAY = 20 * 1000L
        private const val SERVICE_TOKEN_FILE_PATH = "src/main/resources/huggingface/huggingfacekey"
    }
}