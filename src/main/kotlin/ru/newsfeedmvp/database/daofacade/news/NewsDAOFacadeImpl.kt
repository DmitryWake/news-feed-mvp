package ru.newsfeedmvp.database.daofacade.news

import org.jetbrains.exposed.sql.*
import ru.newsfeedmvp.database.DatabaseFactory.dbQuery
import ru.newsfeedmvp.database.table.NewsTable
import ru.newsfeedmvp.features.datasource.rss.model.base.NewsModel

class NewsDAOFacadeImpl : NewsDAOFacade {
    override suspend fun getBySourceUrl(sourceUrl: String): NewsModel? = dbQuery {
        NewsTable.select { NewsTable.sourceUrl eq sourceUrl }.map(::resultRowToNewsModel).singleOrNull()
    }

    override suspend fun getPagingFeed(count: Int, offset: Long): List<NewsModel> = dbQuery {
        NewsTable.selectAll().orderBy(NewsTable.date to SortOrder.ASC).limit(count, offset).map(::resultRowToNewsModel)
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
            it[imageUrl] = model.imageUrl.orEmpty()
            it[sourceUrl] = model.sourceUrl
            it[date] = model.date
            it[viewsCount] = model.viewsCount
            it[likesCount] = model.likesCount
            it[repostsCount] = model.repostsCount
            it[commentsCount] = model.commentsCount
            it[reaction] = model.reactionType
        } > 0
    }

    override suspend fun addNewEntity(model: NewsModel): NewsModel? = dbQuery {
        val insertStatement = NewsTable.insert {
            model.id?.let { mId -> it[id] = mId }
            it[newsBody] = model.newsBody
            it[imageUrl] = model.imageUrl.orEmpty()
            it[sourceUrl] = model.sourceUrl
            it[date] = model.date
            it[viewsCount] = model.viewsCount
            it[likesCount] = model.likesCount
            it[repostsCount] = model.repostsCount
            it[commentsCount] = model.commentsCount
            it[reaction] = model.reactionType
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToNewsModel)
    }

    private fun resultRowToNewsModel(row: ResultRow) = NewsModel(
        id = row[NewsTable.id],
        newsBody = row[NewsTable.newsBody],
        imageUrl = row[NewsTable.imageUrl],
        sourceUrl = row[NewsTable.sourceUrl],
        date = row[NewsTable.date],
        viewsCount = row[NewsTable.viewsCount],
        likesCount = row[NewsTable.likesCount],
        repostsCount = row[NewsTable.repostsCount],
        commentsCount = row[NewsTable.commentsCount],
        reactionType = row[NewsTable.reaction]
    )

    companion object {
        val instance by lazy { NewsDAOFacadeImpl() }
    }
}