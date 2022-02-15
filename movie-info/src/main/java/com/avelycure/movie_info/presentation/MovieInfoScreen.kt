package com.avelycure.movie_info.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    LaunchedEffect(key1 = Unit) {
        getMovieInfo(id)
    }

    Column {
        Trailer()

        MainInfo(state.movieInfo)

        Images()
    }

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
    Column {
        Text(text = movieInfo.title)
        Text(text = movieInfo.tagline)

        val column1Weight = .4f
        val column2Weight = .6f

        LazyColumn(Modifier.wrapContentHeight()) {
            item {
                Row {
                    TableCell(text = "Genre", weight = column1Weight)
                    TableCell(text = movieInfo.getGenresString(), weight = column2Weight)
                }
            }
            item {
                Row {
                    TableCell(text = "Countries", weight = column1Weight)
                    TableCell(text = movieInfo.getCountriesString(), weight = column2Weight)
                }
            }
            item {
                Row {
                    TableCell(text = "Companies", weight = column1Weight)
                    TableCell(text = movieInfo.getCompaniesString(), weight = column2Weight)
                }
            }
            item {
                Row {
                    TableCell(text = "Budget", weight = column1Weight)
                    TableCell(text = movieInfo.budget.toString(), weight = column2Weight)
                }
            }
            item {
                Row {
                    TableCell(text = "Revenue", weight = column1Weight)
                    TableCell(text = movieInfo.revenue.toString(), weight = column2Weight)
                }
            }
            item {
                Row {
                    TableCell(text = "Cast", weight = column1Weight)
                    TableCell(text = movieInfo.getCastString(), weight = column2Weight)
                }
            }
        }
    }

}

@Composable
fun Characteristic(attribute: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = attribute)
        Text(text = value)
    }
}

@Composable
fun Images() {

}