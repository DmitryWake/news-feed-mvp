package ru.newsfeedmvp.features.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.newsfeedmvp.core.model.UserModel
import ru.newsfeedmvp.plugins.AUTH_TYPE_BEARER
import java.util.*

fun Routing.configureUserRouting() {
    authenticate(AUTH_TYPE_BEARER) {
        post("/user/create") {
            val newUserModel = UserModel(id = UUID.randomUUID().toString(), registerDate = Date().time)
            val result = kotlin.runCatching { UserInteractor.instance.createUser(newUserModel) }.getOrNull()

            if (result != null) {
                call.respond(status = HttpStatusCode.OK, message = result)
            } else {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = "Unable to create User with following data: $newUserModel"
                )
            }
        }
    }
}