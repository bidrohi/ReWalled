package com.bidyut.tech.rewalled.di

import android.content.Context
import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.cache.DatabaseDriverFactory
import com.bidyut.tech.rewalled.data.WallpaperRepository
import com.bidyut.tech.rewalled.service.reddit.RedditService
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

class AndroidAppGraph(
    private val appCtx: Context,
    enableDebug: Boolean = false,
) : AppGraph() {

    private val redditService by lazy {
        RedditService(
            HttpClient(Android) {
                baseConfiguration(enableDebug)
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
