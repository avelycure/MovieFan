package com.avelycure.movie_info.presentation

sealed class MovieInfoEvents {

    object OnRemoveHeadFromQueue: MovieInfoEvents()

    data class OnOpenInfoFragment(
        val movieId: Int
    ): MovieInfoEvents()

    object VideoIsUploaded: MovieInfoEvents()
}