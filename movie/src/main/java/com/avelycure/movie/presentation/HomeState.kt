package com.avelycure.movie.presentation

import android.util.Log
import com.avelycure.domain.models.Movie
import com.avelycure.domain.state.ProgressBarState

data class HomeState(
    val movies: List<Movie> = emptyList(),
    val progressBarState: ProgressBarState = ProgressBarState.Idle
) {
    override fun equals(other: Any?): Boolean {
        val result = super.equals(other)
        Log.d("mytag", "Called equals of Home state: " + result)
        return result
    }
}