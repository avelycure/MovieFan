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
import com.avelycure.moviefan.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var crashReporter: CrashReporter
    private lateinit var imageLoader: ImageLoader
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = Handler(mainLooper)
        crashReporter = CrashReporter(handler, lifecycle, applicationContext)
        crashReporter.registerObserver()
        imageLoader = ImageLoader(this, R.drawable.placeholder)
        fragmentManager = supportFragmentManager

        loadHomeScreen()
    }

    private fun loadHomeScreen() {
        fragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, HomeFragment())
            .commit()
    }
}