package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import co.touchlab.kermit.Logger
import com.bidyut.tech.bhandar.ReadResult
import com.bidyut.tech.rewalled.data.SubredditFeedRepository
import com.bidyut.tech.rewalled.di.AppGraph
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.SubReddit
import com.bidyut.tech.rewalled.model.SubredditFeed
import kotlinx.coroutines.flow.map

class CategoriesViewModel(
    private val log: Logger,
    private val repository: SubredditFeedRepository,
) : ViewModel() {

    val subReddits = SubReddit.defaults.toMutableStateList()
    private val uiStateBySubreddit = mutableMapOf<String, State<UiState>>()

    @Composable
    fun getUiState(
        subReddit: String,
    ): State<UiState> = uiStateBySubreddit.getOrPut(subReddit) {
        repository.getWallpaperFeed(
            subReddit,
            Filter.Rising
        ).map { result ->
            log.d("-> read: ${result::class}")
            when (result) {
                is ReadResult.Loading -> UiState.Loading
                is ReadResult.Data -> UiState.ShowContent(result.data)
                is ReadResult.Error -> UiState.Error
            }
        }.collectAsState(UiState.Loading, viewModelScope.coroutineContext)
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
                CategoriesViewModel(
                    appGraph.log,
                    appGraph.subredditFeedRepository,
                )
            }
        }
    }
}
