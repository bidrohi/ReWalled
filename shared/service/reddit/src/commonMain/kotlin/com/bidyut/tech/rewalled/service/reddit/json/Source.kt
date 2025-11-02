package com.bidyut.tech.rewalled.service.reddit.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Source(
    @SerialName("url") val url: String = "",
    @SerialName("width") val width: Int = 0,
    @SerialName("height") val height: Int = 0,
)
