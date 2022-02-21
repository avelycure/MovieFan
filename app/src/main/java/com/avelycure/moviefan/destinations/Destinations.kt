package com.avelycure.moviefan.destinations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.avelycure.image_loader.ImageLoader
import com.avelycure.movie.presentation.HomeScreen
import com.avelycure.movie.presentation.HomeViewModel
import com.avelycure.movie_info.presentation.MovieInfoScreen
import com.avelycure.movie_info.presentation.MovieInfoViewModel
import com.avelycure.moviefan.constants.AppConstants.DEFAULT_MOVIE_ID
import com.avelycure.person.presentation.PersonScreen
import com.avelycure.person.presentation.PersonViewModel

fun NavGraphBuilder.addHomeScreen(
    navController: NavController,
    imageLoader: ImageLoader
) {
    composable(route = "popular_movies") {
        val homeViewModel: HomeViewModel = hiltViewModel()
        HomeScreen(
            state = homeViewModel.state.value,
            fetchPopularMovies = homeViewModel::fetchPopularMovies,
        { id: Int ->
            navController.navigate("movieInfo/${id}")
        },
            {
                url, target -> imageLoader.loadImage(url, target)
            }
        )
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

fun NavGraphBuilder.addPersonsScreen(
    navController: NavController
) {
    composable(route = "persons") {
        val personsViewModel: PersonViewModel = hiltViewModel()
        PersonScreen(
            state = personsViewModel.state.value,
            fetchPopularPerson = personsViewModel::getPopularPerson,
            showMoreInfo = {}
        )
    }
}