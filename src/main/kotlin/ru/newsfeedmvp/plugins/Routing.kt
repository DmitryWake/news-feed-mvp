package ru.newsfeedmvp.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.newsfeedmvp.features.newsuserscore.configureNewsUserScoreRouting
import ru.newsfeedmvp.features.user.configureUserRouting

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        configureUserRouting()
        configureNewsUserScoreRouting()
    }
}
