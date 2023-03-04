package com.bidyut.tech.rewalled.model

typealias FeedId = String

fun makeFeedId(
    subreddit: String,
    filter: Filter,
) = "$subreddit-$filter"

data class Feed(
    val id: FeedId,
    val wallpapers: List<Wallpaper> = emptyList(),
    val afterCursor: String? = null,
)
