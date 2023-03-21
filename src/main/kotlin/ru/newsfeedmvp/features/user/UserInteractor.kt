package ru.newsfeedmvp.features.user

import ru.newsfeedmvp.core.model.UserModel

class UserInteractor {

    private val userRepository = UserRepository.instance

    suspend fun createUser(model: UserModel) = userRepository.createUser(model)
    suspend fun getUsers() = userRepository.getUsers()
    suspend fun isUserExists(id: String) = userRepository.getUser(id) != null


    companion object {
        val instance by lazy { UserInteractor() }
    }
}