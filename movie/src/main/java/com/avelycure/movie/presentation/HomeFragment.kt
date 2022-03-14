package com.avelycure.movie.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.core_navigation.IInstantiator
import com.avelycure.core_navigation.NavigationConstants
import com.avelycure.core_navigation.NavigationConstants.GET_MORE_INFO
import com.avelycure.core_navigation.NavigationConstants.NAVIGATOR
import com.avelycure.core_navigation.Navigator
import com.avelycure.movie.R
import com.avelycure.movie.constants.HomeConstants.NUMBER_OF_FETCHED_MOVIES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var loadImages: (String, ImageView) -> Unit
    private lateinit var openMovieInfo: (Int, navigator: Navigator) -> Unit
    private lateinit var compas: Navigator

    companion object Instantiator : IInstantiator {
        private const val tag = "HOME"
        override fun getTag(): String {
            return tag
        }

        override fun getInstance(bundle: Bundle): Fragment {
            val homeFragment = HomeFragment()
            homeFragment.arguments = bundle
            return homeFragment
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        loadImages =
            arguments?.getSerializable(NavigationConstants.LOAD_IMAGES) as? (String, ImageView) -> Unit
                ?: { _, _ -> }

        openMovieInfo = arguments?.getSerializable(GET_MORE_INFO) as? (Int, navigator: Navigator) -> Unit
            ?: { _ ,_-> }

        compas = (arguments?.getSerializable(NAVIGATOR) as? Navigator)!!

        initViewElements(view)

        lifecycleScope.launchWhenStarted {
            homeViewModel.state.collect { homeState ->
                val insertPos = adapter.data.size
                adapter.data = homeState.movies
                adapter.notifyItemRangeInserted(insertPos, NUMBER_OF_FETCHED_MOVIES)
                adapter.loading = false
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
        adapter = HomeAdapter()
        adapter.loadImages = loadImages
        homeRecyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        homeRecyclerView.adapter = adapter

        adapter.onClickedItem = openMovieInfo
        adapter.compas = compas
        adapter.fetchMore = homeViewModel::fetchPopularMovies
    }
}