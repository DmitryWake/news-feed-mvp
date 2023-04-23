package ru.newsfeedmvp.database.daofacade.recommendation

import org.jetbrains.exposed.sql.*
import ru.newsfeedmvp.database.DatabaseFactory.dbQuery
import ru.newsfeedmvp.database.model.UserRecommendationEntity
import ru.newsfeedmvp.database.table.UserRecommendationTable

class UserRecommendationDAOFacadeImpl : UserRecommendationDAOFacade {
    override suspend fun getByUser(userId: String): UserRecommendationEntity? = dbQuery {
        UserRecommendationTable.select { UserRecommendationTable.userId eq userId }.singleOrNull()
            ?.let(::toUserRecommendationEntity)
    }

    override suspend fun allEntities(): List<UserRecommendationEntity> = dbQuery {
        UserRecommendationTable.selectAll().map(::toUserRecommendationEntity)
    }

    override suspend fun entity(id: Int): UserRecommendationEntity? = dbQuery {
        UserRecommendationTable.select { UserRecommendationTable.id eq id }.singleOrNull()
            ?.let(::toUserRecommendationEntity)
    }

    override suspend fun addNewEntity(model: UserRecommendationEntity): UserRecommendationEntity? = dbQuery {
        UserRecommendationTable.insert {
            it[userId] = model.userId
            it[scoresCount] = model.scoresCount
            it[successfulScoresCount] = model.successfulScoresCount
        }.resultedValues?.singleOrNull()?.let(::toUserRecommendationEntity)
    }

    override suspend fun editEntity(model: UserRecommendationEntity): Boolean = dbQuery {
        UserRecommendationTable.update({ UserRecommendationTable.id eq model.id }) {
            it[userId] = model.userId
            it[scoresCount] = model.scoresCount
            it[successfulScoresCount] = model.successfulScoresCount
        } > 0
    }

    private fun toUserRecommendationEntity(row: ResultRow) = UserRecommendationEntity(
        id = row[UserRecommendationTable.id].value,
        userId = row[UserRecommendationTable.userId],
        scoresCount = row[UserRecommendationTable.scoresCount],
        successfulScoresCount = row[UserRecommendationTable.successfulScoresCount]
    )

    companion object {
        val instance by lazy<UserRecommendationDAOFacade> { UserRecommendationDAOFacadeImpl() }
    }
}