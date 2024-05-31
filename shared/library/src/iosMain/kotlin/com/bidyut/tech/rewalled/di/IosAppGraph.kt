package com.bidyut.tech.rewalled.di

import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.cache.DatabaseDriverFactory
import com.bidyut.tech.rewalled.data.WallpaperRepository
import com.bidyut.tech.rewalled.service.reddit.RedditService
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

class IosAppGraph(
    enableDebug: Boolean = false,
) : AppGraph() {

    private val redditService: RedditService by lazy {
        RedditService(
            HttpClient(Darwin) {
                engine {
                    configureRequest {
                        setAllowsCellularAccess(true)
                    }
                }
                baseConfiguration(enableDebug)
            }
        )
    }

    override val repository: WallpaperRepository by lazy {
        WallpaperRepository(
            Database(DatabaseDriverFactory()),
            redditService,
        )
    }
}
