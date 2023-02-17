package ru.newsfeedmvp.database.daofacade

interface DAOFacade<Model, Id> {
    suspend fun allEntities(): List<Model>
    suspend fun entity(id: Id): Model?
    suspend fun addNewEntity(model: Model): Model?
    suspend fun editEntity(model: Model): Boolean
    // suspend fun deleteArticle(id: Id): Boolean
}