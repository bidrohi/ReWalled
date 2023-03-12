package com.bidyut.tech.rewalled.data

import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.model.*
import com.bidyut.tech.rewalled.service.reddit.RedditService
import com.bidyut.tech.rewalled.service.reddit.json.Post
import com.bidyut.tech.rewalled.service.reddit.json.Source
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WallpaperRepository(
    private val database: Database,
    private val service: RedditService,
) {
    fun getWallpaper(
        id: WallpaperId,
    ): Flow<Wallpaper> = database.getWallpaper(id)

    private suspend fun fetchWallpapersIfEmpty(
        subreddit: String,
        filter: Filter,
        feed: Feed,
    ): Result<Feed> =
        if (feed.wallpapers.isEmpty()) {
            fetchWallpapers(subreddit, filter)
        } else {
            Result.success(feed)
        }

    fun getWallpaperFeed(
        subreddit: String,
        filter: Filter,
    ): Flow<Result<Feed>> =
        database.getWallpaperFeed(makeFeedId(subreddit, filter))
            .map {
                fetchWallpapersIfEmpty(subreddit, filter, it)
            }

    suspend fun getWallpapersAsync(
        subreddit: String,
        filter: Filter,
    ): Feed =
        database.getWallpaperFeedAsync(makeFeedId(subreddit, filter))
            .let { feed ->
                fetchWallpapersIfEmpty(subreddit, filter, feed)
                    .getOrThrow()
            }

    private suspend fun fetchWallpapers(
        subreddit: String,
        filter: Filter,
    ): Result<Feed> =
        service.getPosts(subreddit, filter)
            .fold(
                onSuccess = { response ->
                    val feedId = makeFeedId(subreddit, filter)
                    Result.success(
                        response.data?.children?.let { children ->
                            val wallpapers = children.flatMap {
                                it.post?.toImageList()
                                    .orEmpty()
                            }
                            database.insertWallpapersOnFeedAfter(
                                feedId,
                                wallpapers,
                                response.data?.after
                            )
                            Feed(
                                id = feedId,
                                wallpapers = wallpapers,
                                afterCursor = response.data?.after,
                            )
                        } ?: Feed(id = feedId)
                    )
                },
                onFailure = {
                    Result.failure(it)
                }
            )

    suspend fun fetchMoreWallpapersAfter(
        subreddit: String,
        filter: Filter,
        afterCursor: String,
    ): Result<Boolean> {
        return service.getPosts(subreddit, filter, after = afterCursor)
            .fold(
                onSuccess = { response ->
                    val wallpapers = response.data?.children?.flatMap {
                        it.post?.toImageList()
                            .orEmpty()
                    }
                    wallpapers?.let {
                        database.insertWallpapersOnFeedAfter(
                            makeFeedId(subreddit, filter),
                            it,
                            response.data?.after,
                        )
                    }
                    Result.success(true)
                },
                onFailure = {
                    Result.failure(it)
                },
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
    }
        .orEmpty()

    private fun Source.toImageDetails(): ImageDetail = ImageDetail(
        url = url,
        width = width,
        height = height,
    )
}
