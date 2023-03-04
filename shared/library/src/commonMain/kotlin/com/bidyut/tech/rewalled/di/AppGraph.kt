package com.bidyut.tech.rewalled.di

import com.bidyut.tech.rewalled.data.WallpaperRepository

interface AppGraph {
    val repository: WallpaperRepository

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
