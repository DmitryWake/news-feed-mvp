package ru.newsfeedmvp.database.daofacade.news

import org.jetbrains.exposed.sql.*
import ru.newsfeedmvp.database.DatabaseFactory.dbQuery
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
            it[newsBody] = model.newsBody
            it[imageUrl] = model.imageUrl
            it[sourceUrl] = model.sourceUrl
        } > 0
    }

    override suspend fun addNewEntity(model: NewsModel): NewsModel? = dbQuery {
        val insertStatement = NewsTable.insert {
            it[newsBody] = model.newsBody
            it[imageUrl] = model.imageUrl
            it[sourceUrl] = model.sourceUrl
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToNewsModel)
    }

    private fun resultRowToNewsModel(row: ResultRow) = NewsModel(
        id = row[NewsTable.id],
        newsBody = row[NewsTable.newsBody],
        imageUrl = row[NewsTable.imageUrl],
        sourceUrl = row[NewsTable.sourceUrl]
    )
}