package com.avelycure.moviefan.presentation

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.avelycure.anr_checking.CrashReporter
import com.avelycure.core_navigation.DirectoryStack
import com.avelycure.core_navigation.Navigator
import com.avelycure.domain.constants.MovieConstants.GET_MORE_INFO
import com.avelycure.domain.constants.MovieConstants.MOVIE_ID
import com.avelycure.image_loader.ImageLoader
import com.avelycure.movie.presentation.HomeFragment
import com.avelycure.movie_info.presentation.MovieInfoFragment
import com.avelycure.moviefan.R
import com.avelycure.navigation.Compas
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var crashReporter: CrashReporter
    private lateinit var imageLoader: ImageLoader
    private lateinit var fragmentManager: FragmentManager

    @Inject lateinit var compas: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = Handler(mainLooper)
        crashReporter = CrashReporter(handler, lifecycle, applicationContext)
        crashReporter.registerObserver()
        imageLoader = ImageLoader(this, R.drawable.placeholder)
        fragmentManager = supportFragmentManager

        setUpCompas()

        navigateHome()
    }

    private fun setUpCompas() {
        compas.setHomeFragment(
            c = this,
            rootFragments = listOf(
                DirectoryStack(
                    "MOVIES", mutableListOf()
                ),
                DirectoryStack(
                    "PERSONS", mutableListOf()
                )
            ),
            insts = listOf(
                HomeFragment.Instantiator,
                MovieInfoFragment.Instantiator
            ),
            id = R.id.fragment_container, finish = this::finish
        )
    }

    private fun navigateHome() {
        compas.add(
            directory = "MOVIES",
            fragmentName = HomeFragment.Instantiator.getTag(),
            bundle = Bundle().apply {
                putSerializable(GET_MORE_INFO, { id: Int ->
                    compas.add(
                        "MOVIES",
                        MovieInfoFragment.Instantiator.getTag(),
                        Bundle().apply {
                            putInt(MOVIE_ID, id)
                        }
                    )
                } as Serializable)
            }
        )
    }

    override fun onBackPressed() {
        compas.back()
    }
}