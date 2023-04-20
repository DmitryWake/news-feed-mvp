package ru.newsfeedmvp.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object RecommendationTable : IntIdTable() {
    val userId = reference("userId", UserTable.id)
    val newsId = reference("newsId", NewsTable.id)
    val score = double("score")
}