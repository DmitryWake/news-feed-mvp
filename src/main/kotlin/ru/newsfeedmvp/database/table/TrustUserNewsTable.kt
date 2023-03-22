package ru.newsfeedmvp.database.table

import org.jetbrains.exposed.sql.Table

object TrustUserNewsTable: Table() {
    val id = integer("id").autoIncrement()
    val userId = reference("userId", UserTable.id)
    val newsId = reference("newsId", NewsTable.id)
    val trustValue = bool("trustValue")

    override val primaryKey = PrimaryKey(id)
}