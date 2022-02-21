package com.avelycure.moviefan.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.avelycure.anr_checking.CrashReporter
import com.avelycure.moviefan.destinations.addHomeScreen
import com.avelycure.moviefan.destinations.addMovieInfoScreen
import com.avelycure.moviefan.destinations.addPersonsScreen
import com.avelycure.moviefan.presentation.bottom_bar.BottomNavigationBar
import com.avelycure.resources.theme.MovieFanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavHostController
    private lateinit var handler: Handler
    private lateinit var crashReporter: CrashReporter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler(mainLooper)
        crashReporter = CrashReporter(handler, lifecycle, applicationContext)
        crashReporter.registerObserver()
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