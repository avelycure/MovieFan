package com.avelycure.moviefan.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.avelycure.anr_checking.CrashReporter
import com.avelycure.image_loader.ImageLoader
import com.avelycure.movie.presentation.HomeFragment
import com.avelycure.movie_info.presentation.MovieInfoFragment
import com.avelycure.moviefan.R
import com.avelycure.navigation.Compas
import com.avelycure.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var crashReporter: CrashReporter
    private lateinit var imageLoader: ImageLoader
    private lateinit var fragmentManager: FragmentManager
    private lateinit var compas: Compas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = Handler(mainLooper)
        crashReporter = CrashReporter(handler, lifecycle, applicationContext)
        crashReporter.registerObserver()
        imageLoader = ImageLoader(this, R.drawable.placeholder)
        fragmentManager = supportFragmentManager
        compas = Compas(this, R.id.fragment_container, listOf("movies", "persons"))

        loadHomeScreen()
    }

    private fun loadHomeScreen() {
        compas.add("movies", "movie_fragment", HomeFragment.getInstance { id: Int ->
            compas.add("movies","movie_info", MovieInfoFragment.getInstance(id))
        })

        /*fragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, HomeFragment.getInstance { id: Int ->
                fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, MovieInfoFragment.getInstance(id))
                    .addToBackStack(null)
                    .commit()
            })
            .addToBackStack(null)
            .commit()*/
    }
}