package com.avelycure.movie_info.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun MovieInfoScreen(
    state: MovieInfoState,
    id: Int,
    getMovieInfo: (Int) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        getMovieInfo(100)
    }
}