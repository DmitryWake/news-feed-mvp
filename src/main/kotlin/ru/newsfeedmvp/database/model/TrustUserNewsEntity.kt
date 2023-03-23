package ru.newsfeedmvp.database.model

data class TrustUserNewsEntity(
    val id: Int? = null,
    val userId: String,
    val newsId: Int,
    val trustValue: Boolean
)
