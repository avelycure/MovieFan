package com.avelycure.movie.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.avelycure.data.constants.RequestConstants
import com.avelycure.domain.models.Movie

@Composable
fun HomeScreen(
    state: HomeState,
    fetchPopularMovies: (Int) -> Unit
) {
    MoviesList(
        movies = state.movies,
        modifier = Modifier
            .width(501.dp)
            .height(501.dp)
            .background(Color.Red)
    )

    Button(onClick = { fetchPopularMovies(1) }) {
        Text(text = "Click me + ${state.movies.size}")
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MoviesList(movies: List<Movie>, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(movies) { movie ->
            MovieCard(movie = movie)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MovieCard(movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(251.dp)
            .background(Color.Blue)
    ) {
        Image(
            painter = rememberImagePainter(RequestConstants.IMAGE + movie.posterPath),
            modifier = Modifier
                .width(200.dp)
                .height(220.dp),
            contentDescription = null
        )
        Column {
            Text(
                text = movie.title, modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
        }
    }
}