package com.avelycure.movie.presentation

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.core_navigation.IInstantiator
import com.avelycure.core_navigation.Navigator
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.image_loader.ImageLoader
import com.avelycure.movie.R
import com.avelycure.movie.utils.MovieDiffUtilCallback
import com.avelycure.resources.getQueryChangeStateFlow
import com.avelycure.settings.presentation.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var searchView: SearchView

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var compas: Navigator

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
        initViewElements(view)
        setHasOptionsMenu(true)

        lifecycleScope.launchWhenStarted {
            homeViewModel.state.collect { homeState ->
                val movieDiffUtilCallback = MovieDiffUtilCallback(adapter.data, homeState.movies)
                val diffUtilResult = DiffUtil.calculateDiff(movieDiffUtilCallback)
                adapter.data.clear()
                adapter.data.addAll(homeState.movies)
                diffUtilResult.dispatchUpdatesTo(adapter)
                adapter.loading = (homeState.progressBarState is ProgressBarState.Loading)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Movies"
        homeViewModel.onTrigger(HomeEvents.OnRequestMoreData)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.home_menu, menu)
        initSearchView(menu)
        homeViewModel.onTrigger(HomeEvents.OnGotTextFlow(searchView.getQueryChangeStateFlow()))

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                compas.add(
                    directory = "MOVIES",
                    tag = SettingsFragment.Instantiator.getTag(),
                    bundle = Bundle()
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViewElements(view: View) {
        homeRecyclerView = view.findViewById(R.id.home_rv_movies)
        adapter = HomeAdapter()
        adapter.loadImages = { url, iv -> imageLoader.loadImage(url, iv) }
        homeRecyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        homeRecyclerView.adapter = adapter

        adapter.onClickedItem = { id ->
            compas.add("MOVIES", "MOVIE_INFO", Bundle().apply {
                putInt("movie_id", id)
            })
        }
        adapter.fetchMore = {
            homeViewModel.onTrigger(HomeEvents.OnRequestMoreData)
        }

    }

    private fun initSearchView(menu: Menu) {
        val searchManager =
            (activity as AppCompatActivity).getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search_view).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo((activity as AppCompatActivity).componentName))
        searchView.setIconifiedByDefault(false)

        // The default state of the homeFragment is showing popular movies, so when we close
        // searchView we are calling fetchPopularMovies()
        (menu.findItem(R.id.search_view) as MenuItem).setOnActionExpandListener(object :
            MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                homeViewModel.onTrigger(HomeEvents.OnModeEnabled(HomeFragmentMode.SEARCH))
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                (activity as AppCompatActivity).invalidateOptionsMenu()
                homeViewModel.onTrigger(HomeEvents.OnModeEnabled(HomeFragmentMode.POPULAR))
                return true
            }
        })
    }

}