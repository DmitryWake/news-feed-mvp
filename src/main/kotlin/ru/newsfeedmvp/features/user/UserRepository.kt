package ru.newsfeedmvp.features.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.newsfeedmvp.core.model.UserModel
import ru.newsfeedmvp.database.daofacade.user.UserDAOFacade
import ru.newsfeedmvp.database.daofacade.user.UserDAOFacadeImpl

class UserRepository {

    private val userDAOFacade: UserDAOFacade = UserDAOFacadeImpl.instance

    suspend fun createUser(model: UserModel) = withContext(Dispatchers.IO) { userDAOFacade.addNewEntity(model) }
    suspend fun getUsers() = withContext(Dispatchers.IO) { userDAOFacade.allEntities() }
    suspend fun getUser(id: String) = withContext(Dispatchers.IO) { userDAOFacade.entity(id) }

    companion object {
        val instance by lazy { UserRepository() }
    }
}