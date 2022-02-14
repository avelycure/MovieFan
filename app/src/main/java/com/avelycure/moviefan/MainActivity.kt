package com.avelycure.moviefan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avelycure.movie.presentation.HomeScreen
import com.avelycure.movie.presentation.HomeViewModel
import com.avelycure.moviefan.ui.theme.MovieFanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    fun NavGraphBuilder.addHomeScreen(
        navController1: NavController
    ){
        composable(route = "popular_movies"){
        val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                state = homeViewModel.state.value,
                fetchPopularMovies = homeViewModel::fetchPopularMovies
            )
        }
    }


    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieFanTheme {
                navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "popular_movies",
                    builder = {
                        addHomeScreen(
                            navController
                        )
                    }
                )
            }
        }
    }
}