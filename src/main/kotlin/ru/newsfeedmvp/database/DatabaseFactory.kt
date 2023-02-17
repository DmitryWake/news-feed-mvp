package ru.newsfeedmvp.database

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ru.newsfeedmvp.database.table.NewsTable

object DatabaseFactory {
    fun init() {
        val driver = "org.postgresql.Driver"
        val url = "jdbc:postgresql://localhost:5432/news-feed-mvp"

        val database = Database.connect(url, driver, "postgres", "")

        transaction(database) { SchemaUtils.create(NewsTable) }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}