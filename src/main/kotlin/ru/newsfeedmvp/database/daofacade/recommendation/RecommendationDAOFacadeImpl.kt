package ru.newsfeedmvp.database.daofacade.recommendation

import org.jetbrains.exposed.sql.*
import ru.newsfeedmvp.database.DatabaseFactory.dbQuery
import ru.newsfeedmvp.database.model.RecommendationEntity
import ru.newsfeedmvp.database.table.RecommendationTable

class RecommendationDAOFacadeImpl : RecommendationDAOFacade {
    override suspend fun getByUser(userId: String, limit: Int, offset: Long): List<RecommendationEntity> = dbQuery {
        RecommendationTable.select { RecommendationTable.userId eq userId }.orderBy(RecommendationTable.score to SortOrder.DESC).limit(limit, offset)
            .map(::resultRowToRecommendationEntity)
    }

    override suspend fun addEntities(data: List<RecommendationEntity>): List<RecommendationEntity> = dbQuery {
        RecommendationTable.batchInsert(data) {
            this[RecommendationTable.userId] = it.userId
            this[RecommendationTable.newsId] = it.newsId
            this[RecommendationTable.score] = it.score
        }.map(::resultRowToRecommendationEntity)
    }

    override suspend fun deleteAll() = dbQuery {
        RecommendationTable.deleteAll()
        Unit
    }

    override suspend fun allEntities(): List<RecommendationEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun entity(id: Int): RecommendationEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun addNewEntity(model: RecommendationEntity): RecommendationEntity? = dbQuery {
        RecommendationTable.insert {
            it[userId] = model.userId
            it[newsId] = model.newsId
            it[score] = model.score
        }.resultedValues?.singleOrNull()?.let(::resultRowToRecommendationEntity)
    }

    override suspend fun editEntity(model: RecommendationEntity): Boolean {
        TODO("Not yet implemented")
    }

    private fun resultRowToRecommendationEntity(row: ResultRow) = RecommendationEntity(
        id = row[RecommendationTable.id].value,
        userId = row[RecommendationTable.userId],
        newsId = row[RecommendationTable.newsId],
        score = row[RecommendationTable.score]
    )

    companion object {
        val instance by lazy { RecommendationDAOFacadeImpl() }
    }
}