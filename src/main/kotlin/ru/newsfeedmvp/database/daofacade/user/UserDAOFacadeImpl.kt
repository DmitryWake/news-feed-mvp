package ru.newsfeedmvp.database.daofacade.user

import org.jetbrains.exposed.sql.*
import ru.newsfeedmvp.core.model.UserModel
import ru.newsfeedmvp.database.DatabaseFactory.dbQuery
import ru.newsfeedmvp.database.table.UserTable

class UserDAOFacadeImpl : UserDAOFacade {
    override suspend fun allEntities(): List<UserModel> = dbQuery {
        UserTable.selectAll().map(::resultRowToUserModel)
    }


    override suspend fun entity(id: String): UserModel? = dbQuery {
        UserTable.select { UserTable.id eq id }.map(::resultRowToUserModel).singleOrNull()
    }

    override suspend fun addNewEntity(model: UserModel): UserModel? = dbQuery {
        UserTable.insert {
            it[id] = model.id
            it[registerDate] = model.registerDate
        }.resultedValues?.singleOrNull()?.let(::resultRowToUserModel)
    }

    override suspend fun editEntity(model: UserModel): Boolean = dbQuery {
        UserTable.update({ UserTable.id eq model.id }) {
            it[registerDate] = model.registerDate
        } > 0
    }

    private fun resultRowToUserModel(row: ResultRow) = UserModel(
        id = row[UserTable.id],
        registerDate = row[UserTable.registerDate]
    )

    companion object {
        val instance by lazy { UserDAOFacadeImpl() }
    }
}