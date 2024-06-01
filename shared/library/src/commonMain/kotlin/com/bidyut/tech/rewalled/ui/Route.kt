package com.bidyut.tech.rewalled.ui

import com.bidyut.tech.rewalled.model.SubredditFeedId

sealed class Route(
    val uri: String,
) {
    data object Categories : Route("categories")

    data object Settings : Route("settings")

    data object SettingsCategories : Route("settings/categories")

    data class Grid(
        private val feedId: SubredditFeedId,
    ) : Route("grid/$feedId")

    data class Wallpaper(
        private val feedId: SubredditFeedId,
        private val id: String,
    ) : Route("wall/$feedId/$id")
}
