package com.avelycure.movie_info.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.avelycure.data.constants.RequestConstants
import com.avelycure.domain.models.MovieInfo
import com.avelycure.movie_info.domain.mappers.getCastString
import com.avelycure.movie_info.domain.mappers.getCompaniesString
import com.avelycure.movie_info.domain.mappers.getCountriesString
import com.avelycure.movie_info.domain.mappers.getGenresString

@Composable
fun MovieInfoScreen(
    state: MovieInfoState,
    id: Int,
    getMovieInfo: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        getMovieInfo(id)
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(4.dp)
            .verticalScroll(scrollState)
    ) {
        Poster(state.movieInfo)

        Trailer()

        MainInfo(state.movieInfo)

        Images()
    }

}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun Poster(movieInfo: MovieInfo) {
    Image(
        painter = rememberImagePainter(RequestConstants.IMAGE + movieInfo.posterPath,
        builder = {size(OriginalSize)}),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun Trailer() {

}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(2.dp)
    )
}

@Composable
fun MainInfo(
    movieInfo: MovieInfo
) {
    Text(text = movieInfo.title)
    Text(text = movieInfo.tagline)

    val column1Weight = .4f
    val column2Weight = .6f

    Row {
        TableCell(text = "Genre", weight = column1Weight)
        TableCell(text = movieInfo.getGenresString(), weight = column2Weight)
    }
    Row {
        TableCell(text = "Countries", weight = column1Weight)
        TableCell(text = movieInfo.getCountriesString(), weight = column2Weight)
    }
    Row {
        TableCell(text = "Companies", weight = column1Weight)
        TableCell(text = movieInfo.getCompaniesString(), weight = column2Weight)
    }
    Row {
        TableCell(text = "Budget", weight = column1Weight)
        TableCell(text = movieInfo.budget.toString(), weight = column2Weight)
    }
    Row {
        TableCell(text = "Revenue", weight = column1Weight)
        TableCell(text = movieInfo.revenue.toString(), weight = column2Weight)
    }
    Row {
        TableCell(text = "Cast", weight = column1Weight)
        TableCell(text = movieInfo.getCastString(), weight = column2Weight)
    }
}

@Composable
fun Images() {

}