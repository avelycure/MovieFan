package com.avelycure.movie.presentation

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.avelycure.data.constants.RequestConstants
import com.avelycure.data.constants.TranslationConstants
import com.avelycure.domain.models.Movie
import com.avelycure.image_loader.target.CustomTarget
import com.avelycure.movie.constants.HomeConstants.BUFFER_SIZE
import com.avelycure.resources.BaseScreen
import com.avelycure.resources.OnBottomReached

@Composable
fun HomeScreen(
    state: HomeState,
    fetchPopularMovies: () -> Unit,
    openMoreInfoScreen: (Int) -> Unit,
    loadImage: (String, CustomTarget) -> Unit
) {
    BaseScreen(
        queue = state.errorQueue,
        progressBarState = state.progressBarState,
        onRemoveHeadFromQueue = {}
    ) {
        MoviesList(
            movies = state.movies,
            fetchPopularMovies = fetchPopularMovies,
            openMoreInfoScreen = openMoreInfoScreen,
            loadImage = loadImage
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MoviesList(
    movies: List<Movie>,
    fetchPopularMovies: () -> Unit,
    openMoreInfoScreen: (Int) -> Unit,
    loadImage: (String, CustomTarget) -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(movies) { movie ->
            MovieCard(
                movie = movie,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(251.dp)
                    .background(MaterialTheme.colors.surface)
                    .padding(vertical = 4.dp)
                    .clickable {
                        openMoreInfoScreen(movie.movieId)
                    },
                loadImage = loadImage
            )
        }
    }
    listState.OnBottomReached(
        buffer = BUFFER_SIZE
    ) {
        fetchPopularMovies()
    }
}


@ExperimentalCoilApi
@Composable
fun MovieCard(movie: Movie,
              modifier: Modifier,
              loadImage: (String, CustomTarget) -> Unit) {
    Row(
        modifier = modifier
    ) {
        var image by remember { mutableStateOf<ImageBitmap?>(null) }
        DisposableEffect(movie.posterPath) {

            val target = object : CustomTarget {
                override fun onResourceReady(bitmap: Bitmap) {
                    image = bitmap.asImageBitmap()
                }
            }

            loadImage(RequestConstants.IMAGE + movie.posterPath, target)

            onDispose {
                image = null
            }
        }

        if (image != null) {
            /*Image(
                painter = rememberImagePainter(RequestConstants.IMAGE + movie.posterPath),
                modifier = Modifier
                    .width(200.dp)
                    .fillMaxHeight(),
                contentDescription = null
            )*/
            Image(
                image!!,
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .fillMaxHeight(),
            )
        }
        Column {
            Text(
                text = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = movie.originalTitle + ", " + movie.releaseDate.take(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                fontSize = 14.sp
            )
            Text(
                text = buildString {
                    for (genreId in movie.genreIds)
                        append(TranslationConstants.movieGenre[genreId] + " ")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                fontSize = 16.sp
            )
        }
    }
}