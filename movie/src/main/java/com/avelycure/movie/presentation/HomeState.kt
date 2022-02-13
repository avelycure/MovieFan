package com.avelycure.movie.presentation

import com.avelycure.domain.models.Movie

data class HomeState(
    val movies: List<Movie> = emptyList()
)