package ru.newsfeedmvp.features.newsuserscore

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.newsfeedmvp.features.newsuserscore.model.UserInputScoreDTO
import ru.newsfeedmvp.plugins.AUTH_TYPE_BEARER
import ru.newsfeedmvp.plugins.checkUserId

fun Routing.configureNewsUserScoreRouting() {
    authenticate(AUTH_TYPE_BEARER) {
        post("/newsUserScore/setScore") {
            kotlin.runCatching { call.request.checkUserId() }
                .onSuccess {
                    val request = kotlin.runCatching {
                        call.receive<UserInputScoreDTO>()
                    }.onFailure { println(it.message) }.getOrNull()
                    if (request != null) {
                        val isSuccess = NewsUserScoreRepository.instance.setScoreByUser(it.id, request.newsId, request.score)
                        if (isSuccess) {
                            val trustIndex = NewsUserScoreRepository.instance.getNewsTrustIndex(request.newsId)
                            call.respond(HttpStatusCode.OK, trustIndex)
                        } else {
                            call.respond(HttpStatusCode.BadRequest)
                        }
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
                .onFailure {
                    call.respond(HttpStatusCode.Forbidden, it.message.toString())
                }
        }
    }
}