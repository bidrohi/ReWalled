package com.bidyut.tech.rewalled.service.reddit.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    @SerialName("kind") val kind: String,
    @SerialName("data") val data: Listing? = null,
)
