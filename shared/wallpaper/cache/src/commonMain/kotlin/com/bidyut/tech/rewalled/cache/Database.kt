package com.bidyut.tech.rewalled.cache

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.bidyut.tech.rewalled.model.SubredditFeed
import com.bidyut.tech.rewalled.model.SubredditFeedId
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.model.WallpaperId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

class Database(
    databaseDriverFactory: DatabaseDriverFactory,
) {
    private val database = RedditDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.redditDatabaseQueries
    private val dispatcher = Dispatchers.Unconfined

    @OptIn(ExperimentalTime::class)
    internal suspend fun insertWallpapersOnFeedBefore(
        feedId: SubredditFeedId,
        wallpapers: List<Wallpaper>,
        beforeCursor: String,
    ) = withContext(Dispatchers.IO) {
        dbQuery.transaction {
            dbQuery.updateFeedBeforeCursor(
                feedId,
                beforeCursor,
                Clock.System.now().plus(1.days).toString()
            )
            val lastSeq = dbQuery.selectTopMostSeq(feedId)
                .executeAsOneOrNull() ?: 0
            // flip the list since we need to count up
            for ((i, wallpaper) in wallpapers.asReversed()
                .withIndex()) {
                insertWallpaper(wallpaper)
                dbQuery.insertFeedSeq(
                    feedId,
                    wallpaper.id,
                    lastSeq + (i + 1)
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun insertWallpapersOnFeedAfter(
        feedId: SubredditFeedId,
        wallpapers: List<Wallpaper>,
        afterCursor: String?,
        replaceAll: Boolean = false,
    ) = withContext(Dispatchers.IO) {
        dbQuery.transaction {
            dbQuery.updateFeedAfterCursor(
                feedId,
                afterCursor,
                Clock.System.now().plus(1.days).toString()
            )
            if (replaceAll) {
                dbQuery.removeFeedOrderByFeedId(feedId)
            }
            val lastSeq = dbQuery.selectBottomMostSeq(feedId)
                .executeAsOneOrNull() ?: 0
            // flip the list since we need to count up
            for ((i, wallpaper) in wallpapers.withIndex()) {
                insertWallpaper(wallpaper)
                dbQuery.insertFeedSeq(
                    feedId,
                    wallpaper.id,
                    lastSeq - (i + 1)
                )
            }
        }
    }

    private fun insertWallpaper(
        wallpaper: Wallpaper,
    ) {
        dbQuery.insertWallpaper(
            wallpaper.id,
            wallpaper.summary,
            wallpaper.author,
            wallpaper.url,
            wallpaper.postUrl,
            Json.encodeToString(wallpaper.source),
            wallpaper.thumbnail,
            Json.encodeToString(wallpaper.resizedImages),
        )
    }

    internal fun getFeedBeforeCursor(
        feedId: SubredditFeedId,
    ) = dbQuery.selectFeedBeforeCursor(feedId)
        .executeAsOneOrNull()?.before_cursor

    internal fun getFeedAfterCursor(
        feedId: SubredditFeedId,
    ) = dbQuery.selectFeedAfterCursor(feedId)
        .executeAsOneOrNull()?.after_cursor

    @OptIn(ExperimentalTime::class)
    private fun getWallpaperFeedQuery(
        feedId: SubredditFeedId,
    ): Query<Wallpaper> =
        dbQuery.selectWallpaperFeedById(
            feedId,
            Clock.System.now().toString(),
            ::mapToWallpaper,
        )

    fun getWallpaperFeed(
        feedId: SubredditFeedId,
    ): Flow<SubredditFeed> =
        getWallpaperFeedQuery(feedId).asFlow()
            .mapToList(dispatcher)
            .map {
                SubredditFeed(
                    id = feedId,
                    wallpapers = it,
                    afterCursor = getFeedAfterCursor(feedId)
                )
            }

    suspend fun getWallpaperFeedAsync(
        feedId: SubredditFeedId,
    ): SubredditFeed = withContext(Dispatchers.IO) {
        SubredditFeed(
            id = feedId,
            wallpapers = getWallpaperFeedQuery(feedId).executeAsList(),
            afterCursor = getFeedAfterCursor(feedId)
        )
    }

    private fun getWallpaperQuery(
        wallpaperId: WallpaperId,
    ): Query<Wallpaper> = dbQuery.selectWallpaperById(wallpaperId, ::mapToWallpaper)

    fun getWallpaper(
        wallpaperId: WallpaperId,
    ): Flow<Wallpaper> = getWallpaperQuery(wallpaperId).asFlow()
        .mapToOne(Dispatchers.Main)

    suspend fun getWallpaperAsync(
        wallpaperId: WallpaperId,
    ): Wallpaper? = withContext(Dispatchers.IO) {
        getWallpaperQuery(wallpaperId).executeAsOneOrNull()
    }

    private fun mapToWallpaper(
        id: String,
        description: String,
        author: String,
        url: String,
        postUrl: String,
        source: String,
        thumbnail: String?,
        resizedImages: String,
    ) = Wallpaper(
        id = id,
        summary = description,
        author = author,
        url = url,
        postUrl = postUrl,
        source = Json.decodeFromString(source),
        thumbnail = thumbnail.orEmpty(),
        resizedImages = Json.decodeFromString(resizedImages),
    )

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllFeedOrder()
            dbQuery.removeAllFeed()
            dbQuery.removeAllWallpapers()
        }
    }
}
