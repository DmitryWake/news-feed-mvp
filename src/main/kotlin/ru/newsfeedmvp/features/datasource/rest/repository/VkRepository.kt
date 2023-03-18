package ru.newsfeedmvp.features.datasource.rest.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.newsfeedmvp.core.client.getMainHttpClient
import ru.newsfeedmvp.features.datasource.rest.consts.VK_URL
import ru.newsfeedmvp.features.datasource.rest.model.VkFeedResponse
import java.io.File

class VkRepository {

    private val serviceApiKey by lazy {
        File("src/main/resources/vkapikey/vkapikey.txt").readLines().first()
    }

    suspend fun getNewsFeed(owner_id: String): VkFeedResponse {
        return getMainHttpClient().get(VK_URL) {
            // contentType(ContentType.Application.Json)
            url {
                path("method/wall.get")
                parameters.append("access_token", serviceApiKey)
                parameters.append("owner_id", owner_id)
                parameters.append("v", "5.131")
            }
        }.body()
    }
}