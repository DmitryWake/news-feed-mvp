package ru.newsfeedmvp.features.datasource.rest.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.newsfeedmvp.core.client.getMainHttpClient
import ru.newsfeedmvp.features.datasource.rest.consts.VK_METHOD_WALL_GET
import ru.newsfeedmvp.features.datasource.rest.consts.VK_METHOD_WALL_GET_COMMENTS
import ru.newsfeedmvp.features.datasource.rest.consts.VK_URL
import ru.newsfeedmvp.features.datasource.rest.model.VkCommentsResponse
import ru.newsfeedmvp.features.datasource.rest.model.VkFeedResponse
import java.io.File

// TODO добавить общий механизм создания запроса для VK
class VkRepository {

    private val serviceApiKey by lazy {
        File(SERVICE_API_KEY_FILE_PATH).readLines().first()
    }

    suspend fun getNewsFeed(ownerId: Int): VkFeedResponse = getMainHttpClient().get(VK_URL) {
            url {
                path(VK_METHOD_WALL_GET)
                parameters.append(ACCESS_TOKEN_HEADER, serviceApiKey)
                parameters.append(OWNER_ID_HEADER, ownerId.toString())
                parameters.append(COUNT_HEADER, COUNT_VALUE.toString())
                parameters.append("v", "5.131")
            }
        }.body()

    //TODO Добавить выгрузку всех комментов с учетом пагинации
    suspend fun getNewsComments(ownerId: Int, postId: Int): VkCommentsResponse = getMainHttpClient().get(VK_URL) {
        url {
            path(VK_METHOD_WALL_GET_COMMENTS)
            parameters.append(ACCESS_TOKEN_HEADER, serviceApiKey)
            parameters.append(OWNER_ID_HEADER, ownerId.toString())
            parameters.append("post_id", postId.toString())
            parameters.append(COUNT_HEADER, COUNT_VALUE.toString())
            parameters.append("v", "5.131")
        }
    }.body()

    companion object {
        private const val SERVICE_API_KEY_FILE_PATH = "src/main/resources/vkapikey/vkapikey.txt"

        private const val ACCESS_TOKEN_HEADER = "access_token"
        private const val OWNER_ID_HEADER = "owner_id"
        private const val COUNT_HEADER = "count"
        private const val COUNT_VALUE = 100
    }
}