package ru.newsfeedmvp.database.table

import org.jetbrains.exposed.sql.Table

object UserTable: Table() {
    val id = varchar("id", 36)
    val registerDate = long("registerDate")

    override val primaryKey = PrimaryKey(id)
}