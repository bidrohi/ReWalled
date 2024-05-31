package com.bidyut.tech.rewalled.di

import co.touchlab.kermit.Logger
import com.bidyut.tech.rewalled.core.network.NetworkFactory
import com.bidyut.tech.rewalled.data.WallpaperRepository
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
    abstract val repository: WallpaperRepository

    internal val log by lazy {
        Logger.withTag("ReWalled")
    }

    protected fun HttpClientConfig<*>.baseConfiguration(
        enableDebug: Boolean = false,
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
                logger = object : KtorLogger {
                    override fun log(message: String) {
                        log.d("HTTP Client: $message")
                    }
                }
                level = LogLevel.INFO
            }
        }
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
