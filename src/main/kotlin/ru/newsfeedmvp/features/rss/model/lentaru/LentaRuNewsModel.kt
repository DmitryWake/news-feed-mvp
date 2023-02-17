package ru.newsfeedmvp.features.rss.model.lentaru

import kotlinx.serialization.SerialName
import nl.adaptivity.xmlutil.serialization.XmlElement

@kotlinx.serialization.Serializable
@SerialName("item")
data class LentaRuNewsModel(
    val enclosure: Enclosure,
    @XmlElement(true)
    val author: String?,
    @XmlElement(true)
    val link: String,
    @XmlElement(true)
    val guid: String,
    @XmlElement(true)
    val description: String,
    @XmlElement(true)
    val title: String,
    @XmlElement(true)
    val category: String,
    @XmlElement(true)
    val pubDate: String
) {
    @kotlinx.serialization.Serializable
    @SerialName("enclosure")
    data class Enclosure(
        val length: String,
        val type: String,
        val url: String
    )
}


