package com.avelycure.movie_picker.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.avelycure.core_navigation.IInstantiator
import com.avelycure.movie_picker.R

class MoviePickerFragment : Fragment() {

    companion object Instantiator : IInstantiator {
        private const val tag = "MoviePickerFragment"
        override fun getTag(): String {
            return tag
        }

        override fun getInstance(bundle: Bundle): Fragment {
            val moviePickerFragment = MoviePickerFragment()
            moviePickerFragment.arguments = bundle;
            return moviePickerFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.movie_picker_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}