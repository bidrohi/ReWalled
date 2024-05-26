package com.bidyut.tech.rewalled.service.reddit.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("title") val title: String,
    @SerialName("author") val author: String,
    @SerialName("subreddit") val subreddit: String,
    @SerialName("subreddit_type") val subredditType: String,
    @SerialName("permalink") val permalink: String,
    @SerialName("url") val url: String,
    @SerialName("thumbnail") val thumbnail: String,
    @SerialName("preview") val preview: Preview? = null,
    @SerialName("over_18") val over18: Boolean = false,
)
