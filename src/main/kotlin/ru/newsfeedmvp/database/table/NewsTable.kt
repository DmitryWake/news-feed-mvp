package ru.newsfeedmvp.database.table

import org.jetbrains.exposed.sql.*

object NewsTable: Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 10000)
    val description = varchar("description", 20000)
    val imageUrl = varchar("imageUrl", 256)
    val sourceUrl = varchar("sourceUrl", 256)

    override val primaryKey = PrimaryKey(id)
}