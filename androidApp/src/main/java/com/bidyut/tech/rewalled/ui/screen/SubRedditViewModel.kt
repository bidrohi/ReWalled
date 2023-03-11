package com.bidyut.tech.rewalled.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bidyut.tech.rewalled.di.AppGraph
import com.bidyut.tech.rewalled.model.Feed
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.model.WallpaperId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SubRedditViewModel : ViewModel() {
    private val repository by lazy {
        AppGraph.instance.repository
    }

    fun getUiState(
        subReddit: String,
        filter: Filter,
    ): Flow<UiState> = repository.getWallpaperFeed(subReddit, filter)
        .map { result ->
            result.fold(
                onSuccess = { UiState.ShowContent(it) },
                onFailure = { UiState.Error }
            )
        }

    fun getWallpaper(
        id: WallpaperId,
    ): Flow<Wallpaper> = repository.getWallpaper(id)

    fun loadMoreAfter(
        subReddit: String,
        filter: Filter,
        moreCursor: String,
    ) {
        viewModelScope.launch {
            repository.fetchMoreWallpapersAfter(subReddit, filter, moreCursor)
        }
    }

    sealed interface UiState {
        object Loading : UiState
        object Error : UiState
        data class ShowContent(
            val feed: Feed,
        ) : UiState
    }
}
