package com.avelycure.movie.presentation

import com.avelycure.domain.models.Movie
import com.avelycure.domain.state.ProgressBarState

data class HomeState(
    val movies: List<Movie> = emptyList(),
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val lastVisiblePage: Int = 1
)