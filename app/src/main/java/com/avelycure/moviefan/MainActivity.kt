package com.avelycure.moviefan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
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
import com.avelycure.moviefan.destinations.addHomeScreen
import com.avelycure.moviefan.destinations.addMovieInfoScreen
import com.avelycure.moviefan.destinations.addPersonsScreen
import com.avelycure.moviefan.ui.bottom_bar.BottomNavigationBar
import com.avelycure.moviefan.ui.theme.MovieFanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieFanTheme {
                navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "popular_movies",
                        builder = {
                            addHomeScreen(
                                navController
                            )
                            addMovieInfoScreen(
                                navController
                            )
                            addPersonsScreen(
                                navController
                            )
                        }
                    )
                }
            }
        }
    }


}