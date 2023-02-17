package ru.newsfeedmvp.database.daofacade

import org.jetbrains.exposed.sql.*
import ru.newsfeedmvp.database.DatabaseFactory.dbQuery
import ru.newsfeedmvp.database.daofacade.news.NewsDAOFacade
import ru.newsfeedmvp.database.table.NewsTable
import ru.newsfeedmvp.features.rss.model.base.NewsModel

class NewsDAOFacadeImpl : NewsDAOFacade {
    override suspend fun getBySourceUrl(sourceUrl: String): NewsModel? = dbQuery {
        NewsTable.select { NewsTable.sourceUrl eq sourceUrl }.map(::resultRowToNewsModel).singleOrNull()
    }

    override suspend fun allEntities(): List<NewsModel> = dbQuery {
        NewsTable.selectAll().map(::resultRowToNewsModel)
    }

    override suspend fun entity(id: Int): NewsModel? = dbQuery {
        NewsTable.select { NewsTable.id eq id }.map(::resultRowToNewsModel).singleOrNull()
    }

    override suspend fun editEntity(model: NewsModel): Boolean = dbQuery {
        NewsTable.update({ NewsTable.id eq requireNotNull(model.id) }) {
            it[title] = model.title
            it[description] = model.description
            it[imageUrl] = model.imageUrl
            it[sourceUrl] = model.sourceUrl
        } > 0
    }

    /*override suspend fun deleteArticle(id: Int): Boolean {
        TODO("Not yet implemented")
    }*/

    override suspend fun addNewEntity(model: NewsModel): NewsModel? = dbQuery {
        val insertStatement = NewsTable.insert {
            it[title] = model.title
            it[description] = model.description
            it[imageUrl] = model.imageUrl
            it[sourceUrl] = model.sourceUrl
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToNewsModel)
    }

    private fun resultRowToNewsModel(row: ResultRow) = NewsModel(
        id = row[NewsTable.id],
        title = row[NewsTable.title],
        description = row[NewsTable.description],
        imageUrl = row[NewsTable.imageUrl],
        sourceUrl = row[NewsTable.sourceUrl]
    )
}