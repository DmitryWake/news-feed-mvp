package ru.newsfeedmvp.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import java.io.File

const val AUTH_TYPE_BEARER = "auth-bearer"

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
