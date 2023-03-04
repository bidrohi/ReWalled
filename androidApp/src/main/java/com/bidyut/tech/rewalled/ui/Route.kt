package com.bidyut.tech.rewalled.ui

sealed class Route(
    val uri: String,
) {
    object Grid : Route("grid")

    data class Wallpaper(
        private val id: String,
    ) : Route("wall/$id")
}
