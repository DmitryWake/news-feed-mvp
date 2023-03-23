package ru.newsfeedmvp.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import ru.newsfeedmvp.core.model.UserModel
import ru.newsfeedmvp.features.user.UserRepository
import java.io.File

const val AUTH_TYPE_BEARER = "auth-bearer"

const val USER_ID_HEADER = "UserId"

private val bearerMobileAuthToken by lazy {
    File("src/main/resources/authtokens/BearerMobileAuth").readLines().first()
}

fun Application.configureAuthentication() = install(Authentication) {
    bearer(AUTH_TYPE_BEARER) {
        realm = "Access to the '/' path"
        authenticate { tokenCredential ->
            if (tokenCredential.token == bearerMobileAuthToken) {
                UserIdPrincipal("mobileUser")
            } else {
                null
            }
        }
    }
}

@Throws(IllegalArgumentException::class)
suspend fun ApplicationRequest.checkUserId(): UserModel {
    val inputUserId = headers[USER_ID_HEADER] ?: throw IllegalArgumentException("UserId is null")

    return UserRepository.instance.getUser(inputUserId)
        ?: throw IllegalArgumentException("Can not find user with this UserId: $inputUserId")
}
