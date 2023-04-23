package ru.newsfeedmvp.database

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ru.newsfeedmvp.database.table.NewsTable
import ru.newsfeedmvp.database.table.RecommendationTable
import ru.newsfeedmvp.database.table.TrustUserNewsTable
import ru.newsfeedmvp.database.table.UserTable

object DatabaseFactory {
    fun init() {
        val driver = "org.postgresql.Driver"
        val url = "jdbc:postgresql://localhost:5432/news-feed-mvp"
        val databasePassword = File("src/main/resources/database/database_password").readLines().first()
        val user = "postgres"

        val database = Database.connect(url, driver, user, databasePassword)

        transaction(database) {
            SchemaUtils.create(NewsTable)
            SchemaUtils.create(UserTable)
            SchemaUtils.create(TrustUserNewsTable)
            SchemaUtils.create(RecommendationTable)
            SchemaUtils.create(UserRecommendationTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}