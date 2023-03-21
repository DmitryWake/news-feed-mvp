package ru.newsfeedmvp.core.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: String,
    val registerDate: Long
)
