package ru.newsfeedmvp.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object UserRecommendationTable: IntIdTable() {
    val userId = reference("userId", UserTable.id)
    val scoresCount = uinteger("scoresCount")
    val successfulScoresCount = uinteger("successfulScoresCount")
}