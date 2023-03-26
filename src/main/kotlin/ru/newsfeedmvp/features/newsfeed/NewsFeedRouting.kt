package ru.newsfeedmvp.features.newsfeed

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.newsfeedmvp.features.newsfeed.model.FeedInputParamsDTO
import ru.newsfeedmvp.plugins.AUTH_TYPE_BEARER
import ru.newsfeedmvp.plugins.checkUserId

fun Routing.configureNewsFeedRouting() {
    authenticate(AUTH_TYPE_BEARER) {
        get("/newsFeed") {
            kotlin.runCatching { call.request.checkUserId() }
                .onSuccess {
                    val request = kotlin.runCatching {
                        call.receive<FeedInputParamsDTO>()
                    }.onFailure { println(it.message) }.getOrNull()

                    if (request != null) {
                        kotlin.runCatching {
                            NewsFeedInteractor.instance.getNewsFeed(
                                it.id,
                                request.count,
                                request.offset
                            )
                        }.onSuccess {
                            call.respond(HttpStatusCode.OK, it)
                        }.onFailure {
                            call.respond(HttpStatusCode.BadRequest, it.message.orEmpty())
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