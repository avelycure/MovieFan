package com.avelycure.movie.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.avelycure.data.constants.RequestConstants
import com.avelycure.domain.models.Movie
import kotlinx.coroutines.flow.collect

@Composable
fun HomeScreen(
    state: HomeState,
    fetchPopularMovies: () -> Unit
) {
    MoviesList(
        movies = state.movies,
        fetchPopularMovies = fetchPopularMovies,
        modifier = Modifier
            .fillMaxSize()
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MoviesList(movies: List<Movie>,
               modifier: Modifier,
               fetchPopularMovies: () -> Unit) {
    val listState = rememberLazyListState()
    LazyColumn(modifier = modifier, state = listState) {
        items(movies) { movie ->
            MovieCard(movie = movie)
        }
    }
    listState.OnBottomReached(
        buffer = 8
    ) {
        fetchPopularMovies()
    }
}

@Composable
fun LazyListState.OnBottomReached(
    buffer: Int = 0,
    loadMore: () -> Unit
) {
    require(buffer >= 0){"buffer cannot be negative"}

    val shouldLoadMore = remember {
        derivedStateOf {

            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1 - buffer
        }
    }
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect { if (it) loadMore() }
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