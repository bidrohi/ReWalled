package com.bidyut.tech.rewalled.di

import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.cache.DatabaseDriverFactory
import com.bidyut.tech.rewalled.data.WallpaperRepository
import com.bidyut.tech.rewalled.service.reddit.RedditService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

class DesktopAppGraph(
    enableDebug: Boolean = false,
) : AppGraph() {

    private val redditService by lazy {
        RedditService(
            HttpClient(CIO) {
                baseConfiguration(enableDebug)
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
