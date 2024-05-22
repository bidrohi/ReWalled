package com.bidyut.tech.rewalled

import android.app.Application
import com.bidyut.tech.rewalled.di.AndroidAppGraph
import com.bidyut.tech.rewalled.di.AppGraph
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance

class ReWalledApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        Firebase.analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        Firebase.performance.setPerformanceCollectionEnabled(!BuildConfig.DEBUG)
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
