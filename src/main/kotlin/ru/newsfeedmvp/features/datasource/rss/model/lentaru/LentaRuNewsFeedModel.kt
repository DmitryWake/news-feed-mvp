package ru.newsfeedmvp.features.datasource.rss.model.lentaru

import kotlinx.serialization.SerialName
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@kotlinx.serialization.Serializable
@SerialName("rss")
data class LentaRuNewsFeedModel(
    val channel: Channel,
    val version: String
) {
    @kotlinx.serialization.Serializable
    @SerialName("image")
    data class Image(
        @XmlElement(true)
        val link: String?,
        @XmlElement(true)
        val width: String?,
        @XmlElement(true)
        val title: String?,
        @XmlElement(true)
        val url: String?,
        @XmlElement(true)
        val height: String?
    )

    @kotlinx.serialization.Serializable
    @XmlSerialName("link", "http://www.w3.org/2005/Atom", "atom")
    data class Atomlink(
        val rel: String?,
        val href: String?,
        val type: String?
    )

    @kotlinx.serialization.Serializable
    @SerialName("channel")
    data class Channel(
        val image: Image?,
        val item: List<LentaRuNewsModel>,
        @XmlElement(true)
        val link: String?,
        @XmlElement(true)
        val description: String,
        @XmlElement(true)
        val language: String?,
        @XmlElement(true)
        val title: String?,
        @XmlElement(true)
        val atomlink: Atomlink?
    )
}

