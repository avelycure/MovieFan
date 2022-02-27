package com.avelycure.movie_info.presentation

import com.avelycure.domain.models.Movie
import com.avelycure.domain.models.MovieInfo
import com.avelycure.domain.models.VideoInfo
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.Queue
import com.avelycure.domain.state.UIComponent

data class MovieInfoState(
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf()),
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val movieInfo: MovieInfo = MovieInfo(
        false, 0, "", "", "", "",
        0f, emptyList(), emptyList(), emptyList(), "", "", 0, "", "",
        0f, 0, "", emptyList(), 0, emptyList(), emptyList(), emptyList()
    ),
    val images: List<String> = emptyList(),
    val similar: List<Movie> = emptyList(),
    val videoIsAvailable: Boolean = false,
    val videoInfo: VideoInfo = VideoInfo(),
    val videoIsUploaded: Boolean = false
)