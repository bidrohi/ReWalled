package com.bidyut.tech.rewalled.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDetail(
    @SerialName("url") val url: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
)
