package com.bidyut.tech.rewalled.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.bidyut.tech.rewalled.di.AppGraph
import com.bidyut.tech.rewalled.model.Feed
import com.bidyut.tech.rewalled.model.FeedId
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.makeFeedId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.mobilenativefoundation.store.store5.StoreReadResponse

class SubRedditViewModel : ViewModel() {
    private val repository by lazy {
        AppGraph.instance.repository
    }

    private val log by lazy {
        Logger.withTag("SubRedditViewModel")
    }

    private val uiStateByFeedId = mutableMapOf<FeedId, Flow<UiState>>()

    fun getUiState(
        subReddit: String,
        filter: Filter,
    ): Flow<UiState> = uiStateByFeedId.getOrPut(
        makeFeedId(subReddit, filter)
    ) {
        repository.getWallpaperFeed(subReddit, filter).map { result ->
            log.d("-> read from ${result.origin}: $result")
            when (result) {
                is StoreReadResponse.Loading -> UiState.Loading
                is StoreReadResponse.Data -> UiState.ShowContent(result.value)
                is StoreReadResponse.Error -> UiState.Error
                is StoreReadResponse.NoNewData -> TODO()
            }
        }
    }

    fun getWallpaperFeed(
        feedId: FeedId,
    ): Flow<Feed> = repository.getCachedWallpaperFeed(feedId)

    fun loadMoreAfter(
        subReddit: String,
        filter: Filter,
        moreCursor: String,
    ) {
        viewModelScope.launch {
            repository.getWallpaperFeed(subReddit, filter, moreCursor)
                .filter { result ->
                    log.d("-> adding more from ${result.origin}: $result")
                    result is StoreReadResponse.Data
                }
                .take(1)
                .collect()
        }
    }

    sealed interface UiState {
        data object Loading : UiState
        data object Error : UiState
        data class ShowContent(
            val feed: Feed,
        ) : UiState
    }
}
