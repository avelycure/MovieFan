package com.avelycure.movie.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.avelycure.domain.models.Movie

@Composable
fun HomeScreen(
    state: HomeState,
    fetchPopularMovies: (Int) -> Unit
) {
    MoviesList(
        movies = state.movies,
        modifier = Modifier
            .width(500.dp)
            .height(500.dp)
            .background(Color.Red)
    )

    Button(onClick = { fetchPopularMovies(1) }) {
        Text(text = "Click me + ${state.movies.size}")
    }

    /*LaunchedEffect(key1 = Unit) {
        fetchPopularMovies(1)
    }*/
}

@Composable
fun MoviesList(movies: List<Movie>, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(movies) { movie ->
            MovieCard(movie = movie)
        }
        item {
            Text(text = "1234")
        }
        item {
            Text(text = "1234")
        }
        item {
            Text(text = "1234")
        }
        item {
            Text(text = "1234")
        }
        item {
            Text(text = "1234")
        }
        item {
            Text(text = "1234")
        }



    }
}

@Composable
fun MovieCard(movie: Movie) {
    Text(text = movie.title, modifier = Modifier
        .width(100.dp)
        .height(100.dp))
}