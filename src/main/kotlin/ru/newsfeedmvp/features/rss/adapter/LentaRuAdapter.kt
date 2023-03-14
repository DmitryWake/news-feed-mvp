package ru.newsfeedmvp.features.rss.adapter

import io.ktor.client.call.*
import io.ktor.client.statement.*
import ru.newsfeedmvp.features.rss.model.base.NewsModel
import ru.newsfeedmvp.features.rss.model.lentaru.LentaRuNewsFeedModel
import ru.newsfeedmvp.features.rss.model.lentaru.LentaRuNewsModel

class LentaRuAdapter : RSSFeedAdapter {

    override suspend fun getNewsFeedModel(response: HttpResponse): List<NewsModel> =
        response.body<LentaRuNewsFeedModel>().channel.item.map(::getNewsModel)


    private fun getNewsModel(inputModel: LentaRuNewsModel): NewsModel = NewsModel(
        newsBody = inputModel.title + " " + inputModel.description,
        imageUrl = inputModel.enclosure.url,
        sourceUrl = inputModel.link
    )
}