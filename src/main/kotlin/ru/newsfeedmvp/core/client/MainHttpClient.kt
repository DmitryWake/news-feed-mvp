package ru.newsfeedmvp.core.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.xml.*
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML

@Volatile
private var mainHttpClient: HttpClient? = null

fun getMainHttpClient() = if (mainHttpClient == null) {
    createHttpClient().also { mainHttpClient = it }
} else {
    requireNotNull(mainHttpClient)
}

private fun createHttpClient() = HttpClient(CIO) {
    install(ContentNegotiation) {
        json (Json { ignoreUnknownKeys = true })
        xml(contentType = ContentType.Any, format = XML {
            // this.recommended()
            this.isCollectingNSAttributes = true
        })
    }
}