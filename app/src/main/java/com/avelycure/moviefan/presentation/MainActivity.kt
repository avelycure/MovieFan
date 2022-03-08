package com.avelycure.moviefan.presentation

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.avelycure.anr_checking.CrashReporter
import com.avelycure.core_navigation.DirectoryStack
import com.avelycure.core_navigation.NavigationConstants.LOAD_IMAGES
import com.avelycure.core_navigation.Navigator
import com.avelycure.domain.constants.MovieConstants.GET_MORE_INFO
import com.avelycure.domain.constants.MovieConstants.MOVIE_ID
import com.avelycure.image_loader.ImageLoader
import com.avelycure.movie.presentation.HomeFragment
import com.avelycure.movie_info.presentation.MovieInfoFragment
import com.avelycure.movie_picker.presentation.MoviePickerFragment
import com.avelycure.moviefan.R
import com.avelycure.person.presentation.PersonFragment
import com.example.office.presentation.LoginFragment
import com.example.office.presentation.OfficeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var crashReporter: CrashReporter
    private lateinit var imageLoader: ImageLoader
    private lateinit var fragmentManager: FragmentManager

    private lateinit var bottomNavigationView: BottomNavigationView

    @Inject
    lateinit var compas: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler = Handler(mainLooper)
        crashReporter = CrashReporter(handler, lifecycle, applicationContext)
        crashReporter.registerObserver()
        imageLoader = ImageLoader(this, R.drawable.placeholder)
        fragmentManager = supportFragmentManager

        setUpRoots()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dir_movies -> {
                    Log.d("mytag", "" + compas.directoryIsNotEmpty("MOVIES"))
                    if (compas.directoryIsNotEmpty("MOVIES"))
                        compas.openLastFragmentInDirectory("MOVIES")
                    true
                }
                R.id.dir_persons -> {
                    if (compas.directoryIsNotEmpty("PERSONS"))
                        compas.openLastFragmentInDirectory("PERSONS")
                    else
                        compas.add(
                            directory = "PERSONS",
                            fragmentName = PersonFragment.Instantiator.getTag(),
                            bundle = Bundle().apply {
                                putSerializable(LOAD_IMAGES, { url: String, id: ImageView ->
                                    imageLoader.loadImage(url, id)
                                } as Serializable)
                            }
                        )
                    true
                }
                R.id.dir_choose_movie -> {
                    if (compas.directoryIsNotEmpty("CHOOSE"))
                        compas.openLastFragmentInDirectory("CHOOSE")
                    else
                        compas.add(
                            directory = "CHOOSE",
                            fragmentName = MoviePickerFragment.Instantiator.getTag(),
                            bundle = Bundle()
                        )
                    true
                }
                R.id.dir_office -> {
                    if (compas.directoryIsNotEmpty("OFFICE"))
                        compas.openLastFragmentInDirectory("OFFICE")
                    else
                        compas.add(
                            directory = "OFFICE",
                            fragmentName = LoginFragment.Instantiator.getTag(),
                            bundle = Bundle()
                        )
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun setUpRoots() {
        compas.setUpNavigation(
            c = this,
            rootFragments = listOf(
                DirectoryStack(
                    "MOVIES", mutableListOf()
                ),
                DirectoryStack(
                    "PERSONS", mutableListOf()
                ),
                DirectoryStack(
                    "CHOOSE", mutableListOf()
                ),
                DirectoryStack(
                    "OFFICE", mutableListOf()
                )
            ),
            insts = listOf(
                HomeFragment.Instantiator,
                MovieInfoFragment.Instantiator,
                PersonFragment.Instantiator,
                MoviePickerFragment.Instantiator,
                OfficeFragment.Instantiator,
                LoginFragment.Instantiator
            ),
            id = R.id.fragment_container, finish = this::finish
        )

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
                            putSerializable(
                                LOAD_IMAGES,
                                { url: String, id: ImageView ->
                                    imageLoader.loadImage(url, id)
                                } as Serializable)
                        }
                    )
                } as Serializable)
                putSerializable(LOAD_IMAGES, { url: String, id: ImageView ->
                    imageLoader.loadImage(url, id)
                } as Serializable)
            }
        )
    }

    override fun onBackPressed() {
        compas.back()
    }
}