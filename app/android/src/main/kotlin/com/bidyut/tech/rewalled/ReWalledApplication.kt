package com.bidyut.tech.rewalled

import android.app.Application
import com.bidyut.tech.rewalled.di.AndroidAppGraph
import com.bidyut.tech.rewalled.di.AppGraph
import io.bitdrift.capture.Capture
import io.bitdrift.capture.Configuration
import io.bitdrift.capture.providers.session.SessionStrategy

class ReWalledApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val shouldCollect = BuildConfig.DEBUG
        Capture.Logger.start(
            apiKey = BuildConfig.BITDRIFT_API_KEY,
            sessionStrategy = SessionStrategy.Fixed(),
            configuration = Configuration(enableFatalIssueReporting = shouldCollect),
        )
        AppGraph.assign(
            AndroidAppGraph(
                applicationContext,
                BuildConfig.DEBUG,
            )
        )
    }

//    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(applicationContext)
//        .crossfade(true)
//        .memoryCache {
//            MemoryCache.Builder(applicationContext)
//                .maxSizePercent(0.25)
//                .build()
//        }
//        .diskCache {
//            DiskCache.Builder()
//                .directory(cacheDir.resolve("cache"))
//                .maxSizeBytes(5 * 1024 * 1024)
//                .build()
//        }
//        .build()
}
