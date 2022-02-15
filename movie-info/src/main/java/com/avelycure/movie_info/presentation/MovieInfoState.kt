package com.avelycure.movie_info.presentation

import com.avelycure.domain.models.MovieInfo
import com.avelycure.domain.state.ProgressBarState

data class MovieInfoState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val movieInfo: MovieInfo = MovieInfo(
        false, 0, "", "", "", "",
        0f, emptyList(), emptyList(), emptyList(), "", "", 0, "", "",
        0f, 0, "", emptyList(), 0, emptyList(), emptyList(), emptyList()
    )
)