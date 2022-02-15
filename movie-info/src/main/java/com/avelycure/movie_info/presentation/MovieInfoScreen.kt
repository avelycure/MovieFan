package com.avelycure.movie_info.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@Composable
fun MovieInfoScreen(
    state: MovieInfoState,
    id: Int,
    getMovieInfo: (Int) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        getMovieInfo(id)
    }

    Column(modifier = Modifier
        .wrapContentHeight()
        .wrapContentWidth()) {
        Text(text = state.movieInfo.title)
    }
}