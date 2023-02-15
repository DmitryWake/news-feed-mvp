package ru.newsfeedmvp

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.newsfeedmvp.core.application.runStartingTasks
import ru.newsfeedmvp.plugins.configureRouting
import ru.newsfeedmvp.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
    configureSerialization()

    runStartingTasks()
}
