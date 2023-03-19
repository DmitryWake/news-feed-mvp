package ru.newsfeedmvp.features.datasource.rest.model

import kotlinx.serialization.Serializable

@Serializable
data class VkCommentsResponse(
    var response : Response? = Response()
) {
    @Serializable
    data class Response(
        var count: Int? = null,
        var items: ArrayList<Items> = arrayListOf(),
        var currentLevelCount: Int? = null,
    )

    @Serializable
    data class Items(
        var id: Int? = null,
        var date: Int? = null,
        var text: String? = null,
        var parentsStack: ArrayList<String> = arrayListOf(),
        var thread: Thread? = Thread()
    )

    @Serializable
    data class Thread(
        var count: Int? = null,
        var items: ArrayList<String> = arrayListOf(),
    )
}
