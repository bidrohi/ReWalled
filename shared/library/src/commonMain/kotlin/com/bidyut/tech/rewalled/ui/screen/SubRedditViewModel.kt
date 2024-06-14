package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import co.touchlab.kermit.Logger
import com.bidyut.tech.bhandar.ReadResult
import com.bidyut.tech.rewalled.data.SubredditFeedRepository
import com.bidyut.tech.rewalled.di.AppGraph
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.SubredditFeed
import com.bidyut.tech.rewalled.model.SubredditFeedId
import com.bidyut.tech.rewalled.model.makeSubredditFeedId
import com.bidyut.tech.rewalled.ui.PlatformCoordinator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class SubRedditViewModel(
    private val log: Logger,
    private val repository: SubredditFeedRepository,
    val coordinator: PlatformCoordinator,
) : ViewModel() {

    private val uiStateByFeedId = mutableMapOf<SubredditFeedId, Flow<UiState>>()

    val subReddit = mutableStateOf("EarthPorn")
    val filter = mutableStateOf(Filter.Rising)
    val feedId: SubredditFeedId
        get() = makeSubredditFeedId(subReddit.value, filter.value)

    fun getUiState(): Flow<UiState> = uiStateByFeedId.getOrPut(
        makeSubredditFeedId(subReddit.value, filter.value)
    ) {
        repository.getWallpaperFeed(subReddit.value, filter.value).map { result ->
            log.d("-> read: ${result::class}")
            when (result) {
                is ReadResult.Loading -> UiState.Loading
                is ReadResult.Data -> UiState.ShowContent(result.data)
                is ReadResult.Error -> UiState.Error
            }
        }
    }

    fun getWallpaperFeed(
        feedId: SubredditFeedId,
    ): Flow<SubredditFeed> = repository.getCachedWallpaperFeed(feedId)

    fun loadMoreAfter(
        moreCursor: String,
    ) {
        viewModelScope.launch {
            repository.getWallpaperFeed(subReddit.value, filter.value, moreCursor)
                .filter { result ->
                    log.d("-> adding more: $result")
                    result is ReadResult.Data
                }
                .take(1)
                .collect()
        }
    }

    sealed interface UiState {
        data object Loading : UiState
        data object Error : UiState
        data class ShowContent(
            val feed: SubredditFeed?,
        ) : UiState
    }

    companion object {
        fun factory() = viewModelFactory {
            initializer {
                val appGraph = AppGraph.instance
                SubRedditViewModel(
                    appGraph.log,
                    appGraph.subredditFeedRepository,
                    appGraph.coordinator,
                )
            }
        }
    }
}
