package com.bidyut.tech.rewalled.di

import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.cache.DatabaseDriverFactory
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.ui.PlatformContext
import com.bidyut.tech.rewalled.ui.PlatformCoordinator
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

class DesktopAppGraph(
    enableDebug: Boolean,
) : AppGraph(enableDebug) {

    override val database by lazy {
        Database(DatabaseDriverFactory())
    }

    override val httpClient by lazy {
        HttpClient(OkHttp) {
            baseConfiguration(enableDebug)
        }
    }

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
}
