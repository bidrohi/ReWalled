package com.bidyut.tech.rewalled.ui

import com.bidyut.tech.rewalled.model.Wallpaper

interface PlatformCoordinator {
    fun triggerShareIntent(
        context: PlatformContext,
        w: Wallpaper,
    )

    fun triggerDownloadIntent(
        context: PlatformContext,
        w: Wallpaper,
    )
}
