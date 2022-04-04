package com.avelycure.settings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.avelycure.core_navigation.IInstantiator
import com.avelycure.core_navigation.Navigator
import com.avelycure.image_loader.ImageLoader
import com.avelycure.settings.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment: Fragment() {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var compas: Navigator

    private val settingsViewModel: SettingsViewModel by viewModels()

    companion object Instantiator : IInstantiator {
        private const val tag = "SETTINGS"
        override fun getTag(): String {
            return tag
        }

        override fun getInstance(bundle: Bundle): Fragment {
            val settingsFragment = SettingsFragment()
            settingsFragment.arguments = bundle
            return settingsFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.settings_fragment, container, false)
        return view
    }

}