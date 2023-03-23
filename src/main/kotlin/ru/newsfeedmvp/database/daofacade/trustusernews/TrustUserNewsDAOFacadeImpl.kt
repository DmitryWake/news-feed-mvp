package ru.newsfeedmvp.database.daofacade.trustusernews

import org.jetbrains.exposed.sql.*
import ru.newsfeedmvp.database.DatabaseFactory.dbQuery
import ru.newsfeedmvp.database.model.TrustUserNewsEntity
import ru.newsfeedmvp.database.table.TrustUserNewsTable

class TrustUserNewsDAOFacadeImpl: TrustUserNewsDAOFacade {
    override suspend fun getEntitiesByUser(userId: String): List<TrustUserNewsEntity> = dbQuery {
        TrustUserNewsTable.select { TrustUserNewsTable.userId eq userId }.map(::resultRowToTrustUserNewsEntity)
    }

    override suspend fun getEntitiesByNews(newsId: Int): List<TrustUserNewsEntity> = dbQuery {
        TrustUserNewsTable.select { TrustUserNewsTable.newsId eq newsId }.map(::resultRowToTrustUserNewsEntity)
    }

    override suspend fun allEntities(): List<TrustUserNewsEntity> = dbQuery {
        TrustUserNewsTable.selectAll().map(::resultRowToTrustUserNewsEntity)
    }

    override suspend fun entity(id: Int): TrustUserNewsEntity? = dbQuery {
        TrustUserNewsTable.select { TrustUserNewsTable.id eq id }.singleOrNull()?.let(::resultRowToTrustUserNewsEntity)
    }

    override suspend fun addNewEntity(model: TrustUserNewsEntity): TrustUserNewsEntity? = dbQuery {
        TrustUserNewsTable.insert {
            it[userId] = model.userId
            it[newsId] = model.newsId
            it[trustValue] = model.trustValue
        }.resultedValues?.singleOrNull()?.let(::resultRowToTrustUserNewsEntity)
    }

    override suspend fun editEntity(model: TrustUserNewsEntity): Boolean = dbQuery {
        if (model.id == null) return@dbQuery false

        TrustUserNewsTable.update({ TrustUserNewsTable.id eq model.id }) {
            it[trustValue] = model.trustValue
        } > 0
    }

    private fun resultRowToTrustUserNewsEntity(row: ResultRow) = TrustUserNewsEntity(
        id = row[TrustUserNewsTable.id],
        userId = row[TrustUserNewsTable.userId],
        newsId = row[TrustUserNewsTable.newsId],
        trustValue = row[TrustUserNewsTable.trustValue]
    )

    companion object {
        val instance by lazy { TrustUserNewsDAOFacadeImpl() }
    }
}