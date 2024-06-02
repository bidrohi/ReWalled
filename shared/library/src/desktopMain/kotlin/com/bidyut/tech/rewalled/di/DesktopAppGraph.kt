package com.bidyut.tech.rewalled.di

import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.cache.DatabaseDriverFactory
import com.bidyut.tech.rewalled.data.WallpaperRepository
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.service.reddit.RedditService
import com.bidyut.tech.rewalled.ui.PlatformContext
import com.bidyut.tech.rewalled.ui.PlatformCoordinator
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

class DesktopAppGraph(
    enableDebug: Boolean = false,
) : AppGraph() {

    override val coordinator by lazy {
        object : PlatformCoordinator {
            override fun triggerShareIntent(
                context: PlatformContext,
                w: Wallpaper,
            ) {
                log.d("Share intent triggered for wallpaper: ${w.id}")
            }

            override fun triggerDownloadIntent(
                context: PlatformContext,
                w: Wallpaper,
            ) {
                log.d("Download intent triggered for wallpaper: ${w.id}")
            }
        }
    }

    private val redditService by lazy {
        RedditService(
            HttpClient(CIO) {
                baseConfiguration(enableDebug)
            }
        )
    }

    override val wallpaperRepository by lazy {
        WallpaperRepository(
            Database(DatabaseDriverFactory()),
            redditService,
        )
    }
}
