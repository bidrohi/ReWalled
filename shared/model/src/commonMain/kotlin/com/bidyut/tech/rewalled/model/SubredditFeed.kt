package com.bidyut.tech.rewalled.model

typealias SubredditFeedId = String

fun makeSubredditFeedId(
    subreddit: String,
    filter: Filter,
): SubredditFeedId = "$subreddit-$filter"

fun SubredditFeedId.dissolve(): Pair<String, Filter> {
    val parts = split('-')
    return parts[0] to Filter.fromString(parts[1])
}

data class SubredditFeed(
    val id: SubredditFeedId,
    override val wallpapers: List<Wallpaper> = emptyList(),
    override val afterCursor: String? = null,
) : Feed {
    override val idStr: String
        get() = id
}
