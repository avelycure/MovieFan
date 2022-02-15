package com.avelycure.moviefan.destinations

import android.os.Bundle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.avelycure.movie.presentation.HomeScreen
import com.avelycure.movie.presentation.HomeViewModel
import com.avelycure.movie_info.presentation.MovieInfoScreen
import com.avelycure.movie_info.presentation.MovieInfoViewModel
import com.avelycure.moviefan.constants.AppConstants.DEFAULT_MOVIE_ID
import com.avelycure.moviefan.constants.AppConstants.MOVIE_ID

fun NavGraphBuilder.addHomeScreen(
    navController: NavController
) {
    composable(route = "popular_movies") {
        val homeViewModel: HomeViewModel = hiltViewModel()
        HomeScreen(
            state = homeViewModel.state.value,
            fetchPopularMovies = homeViewModel::fetchPopularMovies
        ) { id: Int ->
            navController.navigate("movieInfo/${id}")
        }
    }
}

fun NavGraphBuilder.addMovieInfoScreen(
    navController: NavController
) {
    composable(
        route = "movieInfo/{movieId}",
        arguments = listOf(navArgument("movieId") { type = NavType.IntType })
    ) { backStackEntry ->
        val movieInfoViewModel: MovieInfoViewModel = hiltViewModel()
        MovieInfoScreen(
            state = movieInfoViewModel.state.value,
            backStackEntry.arguments?.getInt("movieId") ?: DEFAULT_MOVIE_ID,
            movieInfoViewModel::getDetails
        )
    }
}