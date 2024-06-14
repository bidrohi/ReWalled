package com.bidyut.tech.rewalled.data

import com.bidyut.tech.bhandar.Bhandar
import com.bidyut.tech.bhandar.DataFetcher
import com.bidyut.tech.bhandar.ReadResult
import com.bidyut.tech.bhandar.Storage
import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.ImageDetail
import com.bidyut.tech.rewalled.model.SubredditFeed
import com.bidyut.tech.rewalled.model.SubredditFeedId
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.model.makeSubredditFeedId
import com.bidyut.tech.rewalled.service.reddit.RedditService
import com.bidyut.tech.rewalled.service.reddit.json.Post
import com.bidyut.tech.rewalled.service.reddit.json.Source
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubredditFeedRepository(
    private val database: Database,
    private val service: RedditService,
) : Bhandar<SubredditFeedRepository.Request, SubredditFeed>(
    fetcher = DataFetcher.of { request ->
        service.getPosts(
            request.subreddit,
            request.filter,
            after = request.after,
        ).map { response ->
            val feedId = makeSubredditFeedId(request.subreddit, request.filter)
            response.data?.children?.let { children ->
                SubredditFeed(
                    id = feedId,
                    wallpapers = children.flatMap { item ->
                        item.post?.takeIf { !it.over18 }
                            ?.toImageList()
                            .orEmpty()
                    },
                    afterCursor = response.data?.after,
                )
            } ?: SubredditFeed(id = feedId)
        }
    },
    storage = Storage.Companion.of(
        isValid = { it?.wallpapers?.isNotEmpty() == true },
        read = { request ->
            database.getWallpaperFeed(makeSubredditFeedId(request.subreddit, request.filter))
                .map {
                    if (it.afterCursor != null && it.afterCursor == request.after) {
                        null
                    } else {
                        it
                    }
                }
        },
        write = { request, data ->
            val feedId = makeSubredditFeedId(request.subreddit, request.filter)
            database.insertWallpapersOnFeedAfter(
                feedId,
                data.wallpapers,
                data.afterCursor,
                replaceAll = request.after == null,
            )
        }
    ),
) {
    fun getWallpaperFeed(
        subreddit: String,
        filter: Filter,
        after: String? = null,
    ): Flow<ReadResult<SubredditFeed>> = cached(
        Request(subreddit, filter, after),
        refresh = false,
    )

    fun getCachedWallpaperFeed(
        feedId: SubredditFeedId,
    ): Flow<SubredditFeed> = database.getWallpaperFeed(feedId)

    data class Request(
        val subreddit: String,
        val filter: Filter,
        val after: String? = null,
    )
}

private fun Post.toImageList(): List<Wallpaper> = preview?.images?.map { image ->
    Wallpaper(
        id = id,
        summary = title,
        author = author,
        url = url,
        postUrl = "https://reddit.com$permalink",
        source = image.source.toImageDetails(),
        thumbnail = thumbnail,
        resizedImages = image.resolutions.map {
            it.toImageDetails()
        },
    )
}.orEmpty()

private fun Source.toImageDetails(): ImageDetail = ImageDetail(
    url = url,
    width = width,
    height = height,
)
