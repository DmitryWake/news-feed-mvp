package ru.newsfeedmvp.features.datasource.rest.model

import kotlinx.serialization.Serializable

@Serializable
data class VkFeedResponse(
    val response: Response?
) {
    @Serializable
    data class Response(
        val count: Int? = null,
        val items: ArrayList<Items> = arrayListOf()
    )

    @Serializable
    data class Items(
        val comments: Comments? = Comments(),
        val type: String? = null,
        val attachments: ArrayList<Attachments> = arrayListOf(),
        val date: Long? = null,
        val id: Int,
        val likes: Likes? = Likes(),
        val postType: String? = null,
        val reposts: Reposts? = null,
        val text: String? = null,
        val views: Views? = null
    )

    @Serializable
    data class Views(var count: Int? = null)

    @Serializable
    data class Reposts(
        val count: Int? = null,
        val userReposted: Int? = null
    )

    @Serializable
    data class Likes(
        val canLike: Int? = null,
        val count: Int? = null,
        val userLikes: Int? = null,
        val canPublish: Int? = null,
        val repostDisabled: Boolean? = null
    )

    @Serializable
    data class Attachments(
        var type: String? = null,
        var photo: Photo? = Photo()
    )

    @Serializable
    data class Photo(
        var sizes: ArrayList<Sizes> = arrayListOf(),
    )

    @Serializable
    data class Sizes(
        var height: Int? = null,
        var type: String? = null,
        var width: Int? = null,
        var url: String? = null
    )

    @Serializable
    data class Comments(
        var canPost: Int? = null,
        var count: Int? = null,
        var groupsCanPost: Boolean? = null
    )
}
