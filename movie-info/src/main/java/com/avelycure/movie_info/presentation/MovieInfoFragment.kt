package com.avelycure.movie_info.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.avelycure.domain.constants.MovieConstants.DEFAULT_MOVIE_ID
import com.avelycure.domain.constants.MovieConstants.MOVIE_ID
import com.avelycure.movie_info.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieInfoFragment : Fragment() {

    companion object {
        fun getInstance(id: Int): MovieInfoFragment {
            val movieInfoFragment = MovieInfoFragment()
            val args = Bundle().apply {
                putInt(MOVIE_ID, id)
            }
            movieInfoFragment.arguments = args
            return movieInfoFragment
        }
    }

    private var movieId: Int = DEFAULT_MOVIE_ID

    private val movieInfoViewModel: MovieInfoViewModel by viewModels()

    //private lateinit var movieInfoAdapter: MovieInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.movie_info_fragment, container, false)

        movieId = arguments?.getInt(MOVIE_ID, DEFAULT_MOVIE_ID) ?: DEFAULT_MOVIE_ID



        return view
    }
}