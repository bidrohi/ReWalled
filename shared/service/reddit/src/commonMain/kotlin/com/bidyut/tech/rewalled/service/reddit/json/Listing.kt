package com.bidyut.tech.rewalled.service.reddit.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Listing(
    @SerialName("modhash") val modHash: String,
    @SerialName("after") val after: String? = null,
    @SerialName("before") val before: String? = null,
    @SerialName("children") val children: List<Child> = emptyList(),
)
