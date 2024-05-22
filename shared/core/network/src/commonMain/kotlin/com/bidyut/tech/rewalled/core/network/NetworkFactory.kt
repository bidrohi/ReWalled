package com.bidyut.tech.rewalled.core.network

import kotlinx.serialization.json.Json

object NetworkFactory {
    fun buildJson() = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
}
