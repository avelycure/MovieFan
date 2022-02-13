package com.avelycure.movie.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.avelycure.domain.models.Movie

@Composable
fun HomeScreen(state: HomeState) {
    MoviesList(
        movies = state.movies,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun MoviesList(movies: List<Movie>, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(movies) { movie ->
            MovieCard(movie = movie)
        }
    }
}

@Composable
fun MovieCard(movie: Movie) {
    Text(text = movie.title)
}