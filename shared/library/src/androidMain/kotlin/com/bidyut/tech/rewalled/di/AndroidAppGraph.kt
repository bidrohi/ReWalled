package com.bidyut.tech.rewalled.di

import android.content.Context
import android.util.Log
import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.cache.DatabaseDriverFactory
import com.bidyut.tech.rewalled.core.network.NetworkFactory
import com.bidyut.tech.rewalled.data.WallpaperRepository
import com.bidyut.tech.rewalled.service.reddit.RedditService
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel.INFO
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

class AndroidAppGraph(
    private val appCtx: Context,
    private val enableDebug: Boolean = false,
) : AppGraph {
    private val redditService by lazy {
        RedditService(
            HttpClient(Android) {
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
                                Log.d("HTTP Client", message)
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
            Database(DatabaseDriverFactory(appCtx)),
            redditService,
        )
    }
}
