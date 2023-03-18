package ru.newsfeedmvp.features.datasource.rss.adapter

import io.ktor.client.call.*
import io.ktor.client.statement.*
import ru.newsfeedmvp.features.datasource.NewsFeedInteractor
import ru.newsfeedmvp.features.datasource.rss.model.base.NewsModel
import ru.newsfeedmvp.features.datasource.rss.model.lentaru.LentaRuNewsFeedModel
import ru.newsfeedmvp.features.datasource.rss.model.lentaru.LentaRuNewsModel
import java.util.*

//TODO Работаем пока только с VK
class LentaRuInteractor : NewsFeedInteractor {

     suspend fun getNewsFeedModelOld(response: HttpResponse): List<NewsModel> =
        response.body<LentaRuNewsFeedModel>().channel.item.map(::getNewsModel)


    private fun getNewsModel(inputModel: LentaRuNewsModel): NewsModel = NewsModel(
        newsBody = inputModel.title + " " + inputModel.description,
        imageUrl = inputModel.enclosure.url,
        sourceUrl = inputModel.link,
        date = Date().time
    )

    override suspend fun getNewsFeedModel(): List<NewsModel> {
        TODO("Not yet implemented")
    }
}