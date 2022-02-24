package com.avelycure.movie.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.domain.constants.MovieConstants.GET_MORE_INFO
import com.avelycure.image_loader.ImageLoader
import com.avelycure.movie.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.Serializable

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    companion object{
        fun getInstance(
            getInfo: (Int) -> Unit
        ): HomeFragment {
            val homeFragment = HomeFragment()
            val args=  Bundle().apply {
                putSerializable(GET_MORE_INFO, getInfo as Serializable)
            }
            homeFragment.arguments = args
            return homeFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        initViewElements(view)
        lifecycleScope.launchWhenStarted {
            homeViewModel.state.collect { homeState ->
                adapter.data = homeState.movies
                adapter.notifyDataSetChanged()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.fetchPopularMovies()
    }

    private fun initViewElements(view: View) {
        homeRecyclerView = view.findViewById(R.id.home_rv_movies)
        adapter = HomeAdapter(ImageLoader(view.context, R.drawable.placeholder))
        homeRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        homeRecyclerView.adapter = adapter

        adapter.onClickedItem = arguments?.getSerializable(GET_MORE_INFO) as (Int) -> Unit
    }
}