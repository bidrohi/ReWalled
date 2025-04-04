package com.bidyut.tech.rewalled.ui

import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.SubredditFeedId
import com.bidyut.tech.rewalled.model.WallpaperId
import com.bidyut.tech.rewalled.model.makeSubredditFeedId
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Categories : Route

    @Serializable
    data object Settings : Route

    @Serializable
    data object SettingsCategories : Route

    @Serializable
    data class Grid(
        val feedId: SubredditFeedId = makeSubredditFeedId("EarthPorn", Filter.Rising),
    ) : Route

    @Serializable
    data class Wallpaper(
        val feedId: SubredditFeedId = makeSubredditFeedId("EarthPorn", Filter.Rising),
        val id: WallpaperId? = null,
    ) : Route
}
