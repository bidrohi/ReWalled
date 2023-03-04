package com.bidyut.tech.rewalled.service.reddit.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Preview(
    @SerialName("enabled") val enabled: Boolean,
    @SerialName("images") val images: List<Image> = emptyList(),
)
