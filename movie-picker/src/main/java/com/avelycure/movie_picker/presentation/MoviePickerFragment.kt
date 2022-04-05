package com.avelycure.movie_picker.presentation

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.avelycure.core_navigation.IInstantiator
import com.avelycure.movie_picker.R
import com.example.laroullete.Roulette

class MoviePickerFragment : Fragment() {

    lateinit var roulette: Roulette

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
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.movie_picker_fragment, container, false)
        roulette = view.findViewById(R.id.la_roulette)
        roulette.setData(listOf("Action", "Drama", "Comedy", "Horror", "TvShow", "Cartoon", "War", "History"))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title = "Movie chooser"
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.empty_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}