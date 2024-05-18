package com.bidyut.tech.rewalled.di

import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.cache.DatabaseDriverFactory
import com.bidyut.tech.rewalled.core.network.NetworkFactory
import com.bidyut.tech.rewalled.data.WallpaperRepository
import com.bidyut.tech.rewalled.service.reddit.RedditService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel.INFO
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

class DesktopAppGraph(
    private val enableDebug: Boolean = false,
) : AppGraph {
    private val redditService by lazy {
        RedditService(
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(NetworkFactory.buildJson())
                }
                install(ContentEncoding) {
                    deflate(1.0F)
                    gzip(0.9F)
                }
                install(HttpTimeout)
                install(HttpCache)
                install(UserAgent) {
                    agent = "ReWalled"
                }
                if (enableDebug) {
                    install(Logging) {
                        logger = object : Logger {
                            override fun log(message: String) {
                                println("HTTP Client: $message")
                            }
                        }
                        level = INFO
                    }
                }
            }
        )
    }

    override val repository by lazy {
        WallpaperRepository(
            Database(DatabaseDriverFactory()),
            redditService,
        )
    }
}
