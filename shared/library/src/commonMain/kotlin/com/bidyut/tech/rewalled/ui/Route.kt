package com.bidyut.tech.rewalled.ui

import com.bidyut.tech.rewalled.model.FeedId

sealed class Route(
    val uri: String,
) {
    data object Grid : Route("grid")

    data class Wallpaper(
        private val feedId: FeedId,
        private val id: String,
    ) : Route("wall/$feedId/$id")
}
