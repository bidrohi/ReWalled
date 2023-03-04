package com.bidyut.tech.rewalled.service.reddit.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("id") val id: String,
    @SerialName("source") val source: Source,
    @SerialName("resolutions") val resolutions: List<Source> = emptyList(),
)
