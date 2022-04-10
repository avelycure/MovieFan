package com.avelycure.moviefan.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.avelycure.anr_checking.CrashReporter
import com.avelycure.core_navigation.DirectoryStack
import com.avelycure.core_navigation.Navigator
import com.avelycure.movie.presentation.HomeFragment
import com.avelycure.movie_info.presentation.MovieInfoFragment
import com.avelycure.movie_picker.presentation.MoviePickerFragment
import com.avelycure.moviefan.R
import com.avelycure.person.presentation.PersonFragment
import com.avelycure.settings.presentation.SettingsFragment
import com.example.office.presentation.LoginFragment
import com.example.office.presentation.OfficeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var crashReporter: CrashReporter

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var mainToolbar: Toolbar

    @Inject
    lateinit var compas: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("mytag", "MainOnCreate: " + savedInstanceState?.getString("WIDGET_TYPE", "MOVIE"))
        Log.d("mytag", "MainOnCreate: " + intent.extras?.getString("WIDGET_TYPE", "MOVIE"))

        crashReporter = CrashReporter(applicationContext)
        crashReporter.registerObserver()

        mainToolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dir_movies -> {
                    if (compas.directoryIsNotEmpty("MOVIES"))
                        compas.openLastFragmentInDirectory("MOVIES")
                    else
                        addHomeScreen()
                    true
                }
                R.id.dir_persons -> {
                    if (compas.directoryIsNotEmpty("PERSONS"))
                        compas.openLastFragmentInDirectory("PERSONS")
                    else
                        compas.add(
                            directory = "PERSONS",
                            tag = PersonFragment.Instantiator.getTag(),
                            bundle = Bundle()
                        )
                    true
                }
                R.id.dir_choose_movie -> {
                    if (compas.directoryIsNotEmpty("CHOOSE"))
                        compas.openLastFragmentInDirectory("CHOOSE")
                    else
                        compas.add(
                            directory = "CHOOSE",
                            tag = MoviePickerFragment.Instantiator.getTag(),
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
                            tag = LoginFragment.Instantiator.getTag(),
                            bundle = Bundle()
                        )
                    true
                }
                else -> {
                    false
                }
            }
        }
        setUpRoots(savedInstanceState)
        initHome(savedInstanceState)
        handleIntent()
    }

    private fun handleIntent() {
        val type = intent?.extras?.getString("WIDGET_TYPE", "MOVIE")
        Log.d("mytag", "Type: " + type)
        when (type) {
            "MOVIE" -> compas.openLastFragmentInDirectory("MOVIES")
            "PERSON" -> compas.add(
                directory = "PERSONS",
                tag = PersonFragment.Instantiator.getTag(),
                bundle = Bundle()
            )
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("mytag", "On NewIntent: " + intent?.extras?.getString("WIDGET_TYPE", "MOVIE"))
    }

    private fun setUpRoots(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
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
                    LoginFragment.Instantiator,
                    SettingsFragment.Instantiator
                ),
                id = R.id.fragment_container, finish = this::finish
            )
    }

    private fun initHome(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            addHomeScreen()
        } else
            compas.recreate(this, this::finish, savedInstanceState)
    }

    private fun addHomeScreen() {
        compas.add(
            directory = "MOVIES",
            tag = HomeFragment.Instantiator.getTag(),
            bundle = Bundle()
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        compas.onDestroy(outState)
    }

    override fun onBackPressed() {
        compas.back()
    }
}