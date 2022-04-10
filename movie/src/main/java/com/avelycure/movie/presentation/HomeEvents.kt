package com.avelycure.movie.presentation

import kotlinx.coroutines.flow.Flow

enum class HomeFragmentMode{
    POPULAR, SEARCH
}

sealed class HomeEvents {
    object OnRemoveHeadFromQueue : HomeEvents()

    object OnRequestMoreData : HomeEvents()

    data class OnModeEnabled(val mode: HomeFragmentMode): HomeEvents()

    data class OnGotTextFlow(val queryFlow: Flow<String>) : HomeEvents()
}