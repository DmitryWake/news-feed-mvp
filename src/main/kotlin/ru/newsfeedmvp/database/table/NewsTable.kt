package ru.newsfeedmvp.database.table

import org.jetbrains.exposed.sql.Table

object NewsTable: Table() {
    val id = integer("id").autoIncrement()
    val newsBody = varchar("newsBody", 10000)
    val imageUrl = varchar("imageUrl", 256)
    val sourceUrl = varchar("sourceUrl", 256)
    val viewsCount = integer("viewsCount").nullable()
    val likesCount = integer("likesCount").nullable()
    val repostsCount = integer("repostsCount").nullable()
    val commentsCount = integer("commentsCount").nullable()
    val date = long("date")

    override val primaryKey = PrimaryKey(id)
}