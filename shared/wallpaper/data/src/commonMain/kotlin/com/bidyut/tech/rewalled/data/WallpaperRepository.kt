package com.bidyut.tech.rewalled.data

import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.model.Feed
import com.bidyut.tech.rewalled.model.FeedId
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.ImageDetail
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.model.makeFeedId
import com.bidyut.tech.rewalled.service.reddit.RedditService
import com.bidyut.tech.rewalled.service.reddit.json.Post
import com.bidyut.tech.rewalled.service.reddit.json.Source
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import kotlin.time.Duration.Companion.minutes

class WallpaperRepository(
    private val database: Database,
    private val service: RedditService,
) : Store<WallpaperRepository.Request, Feed> by StoreBuilder.from(
    fetcher = Fetcher.of<Request, Feed> { request ->
        service.getPosts(request.subreddit, request.filter, after = request.after).fold(
            onSuccess = { response ->
                val feedId = makeFeedId(request.subreddit, request.filter)
                response.data?.children?.let { children ->
                    Feed(
                        id = feedId,
                        wallpapers = children.flatMap {
                            it.post?.takeIf { !it.over18 }
                                ?.toImageList()
                                .orEmpty()
                        },
                        afterCursor = response.data?.after,
                    )
                } ?: Feed(id = feedId)
            }, onFailure = {
                throw it
            }
        )
    },
    sourceOfTruth = SourceOfTruth.of(
        reader = { request ->
            database.getWallpaperFeed(makeFeedId(request.subreddit, request.filter))
                .map {
                    if (it.afterCursor != null && it.afterCursor == request.after) {
                        null
                    } else {
                        it
                    }
                }
        },
        writer = { request, feed ->
            val feedId = makeFeedId(request.subreddit, request.filter)
            database.insertWallpapersOnFeedAfter(
                feedId,
                feed.wallpapers,
                feed.afterCursor,
                replaceAll = request.after == null,
            )
        },
    )
).cachePolicy(
    MemoryPolicy.builder<Request, Feed>()
        .setMaxSize(10)
        .setExpireAfterAccess(30.minutes)
        .build()
).build() {

    fun getWallpaperFeed(
        subreddit: String,
        filter: Filter,
        after: String? = null,
    ): Flow<StoreReadResponse<Feed>> = stream(
        StoreReadRequest.cached(
            Request(subreddit, filter, after),
            refresh = true
        )
    )

    fun getCachedWallpaperFeed(
        feedId: FeedId,
    ): Flow<Feed> = database.getWallpaperFeed(feedId)

    internal data class Request(
        val subreddit: String,
        val filter: Filter,
        val after: String? = null,
    )
}

private fun Post.toImageList(): List<Wallpaper> = preview?.images?.map { image ->
    Wallpaper(
        id = id,
        description = title,
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

