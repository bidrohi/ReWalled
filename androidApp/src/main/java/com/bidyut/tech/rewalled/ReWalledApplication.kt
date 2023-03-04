package com.bidyut.tech.rewalled

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.bidyut.tech.rewalled.di.AndroidAppGraph
import com.bidyut.tech.rewalled.di.AppGraph

class ReWalledApplication: Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        AppGraph.assign(
            AndroidAppGraph(
                applicationContext,
                BuildConfig.DEBUG,
            )
        )
    }

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(applicationContext)
        .crossfade(true)
        .memoryCache {
            MemoryCache.Builder(applicationContext)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(cacheDir.resolve("cache"))
                .maxSizeBytes(5 * 1024 * 1024)
                .build()
        }
        .build()
}
