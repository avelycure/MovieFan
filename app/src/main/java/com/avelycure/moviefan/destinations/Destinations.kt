package com.avelycure.moviefan.destinations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.avelycure.movie.presentation.HomeScreen
import com.avelycure.movie.presentation.HomeViewModel
import com.avelycure.movie_info.presentation.MovieInfoScreen
import com.avelycure.movie_info.presentation.MovieInfoViewModel

fun NavGraphBuilder.addHomeScreen(
    navController: NavController
) {
    composable(route = "popular_movies") {
        val homeViewModel: HomeViewModel = hiltViewModel()
        HomeScreen(
            state = homeViewModel.state.value,
            fetchPopularMovies = homeViewModel::fetchPopularMovies,
            {
                navController.navigate("movie_info")
            }
        )
    }
}

fun NavGraphBuilder.addMovieInfoScreen(
    navController: NavController
) {
    composable(route = "movie_info") {
        val movieInfoViewModel: MovieInfoViewModel = hiltViewModel()
        MovieInfoScreen(
            state = movieInfoViewModel.state.value,
            111, movieInfoViewModel::getDetails
        )
    }
}