package com.bidyut.tech.rewalled.di

import co.touchlab.kermit.Logger
import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.core.network.NetworkFactory
import com.bidyut.tech.rewalled.data.SubredditFeedRepository
import com.bidyut.tech.rewalled.service.reddit.RedditService
import com.bidyut.tech.rewalled.ui.PlatformCoordinator
import io.kamel.core.config.Core
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.KamelConfigBuilder
import io.kamel.core.config.httpUrlFetcher
import io.kamel.core.config.takeFrom
import io.kamel.image.config.imageBitmapDecoder
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.logging.Logger as KtorLogger

abstract class AppGraph {
    protected abstract val database: Database
    protected abstract val httpClient: HttpClient
    abstract val kamelConfig: KamelConfig
    abstract val coordinator: PlatformCoordinator

    internal val log by lazy {
        Logger.withTag("ReWalled")
    }

    private val ktorLogger = object : KtorLogger {
        override fun log(message: String) {
            log.d("HTTP Client: $message")
        }
    }

    protected fun HttpClientConfig<*>.baseConfiguration(
        enableDebug: Boolean,
    ) {
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
                logger = ktorLogger
                level = LogLevel.INFO
            }
        }
    }

    protected fun KamelConfigBuilder.baseConfiguration(
        enableDebug: Boolean,
    ) {
        takeFrom(KamelConfig.Core)
        imageBitmapDecoder()
        httpUrlFetcher {
            httpCache(100 * 1024 * 1024)
            if (enableDebug) {
                Logging {
                    logger = ktorLogger
                    level = LogLevel.INFO
                }
            }
        }
    }

    private val redditService by lazy {
        RedditService(httpClient)
    }

    val subredditFeedRepository by lazy {
        SubredditFeedRepository(database, redditService)
    }

    companion object {
        lateinit var instance: AppGraph
            private set

        fun assign(
            graph: AppGraph,
        ) {
            instance = graph
        }
    }
}
