package com.avelycure.movie.presentation

import com.avelycure.domain.models.Movie
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.Queue
import com.avelycure.domain.state.UIComponent

data class HomeState(
    val movies: List<Movie> = emptyList(),
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf()),
    val mode: HomeFragmentMode = HomeFragmentMode.POPULAR
)