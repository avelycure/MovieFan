package com.avelycure.movie.presentation

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
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
import com.avelycure.image_loader.ImageLoader
import com.avelycure.movie.R
import com.avelycure.movie.constants.HomeConstants.NUMBER_OF_FETCHED_MOVIES
import com.avelycure.settings.presentation.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private val homeViewModel: HomeViewModel by viewModels()

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
        (activity as AppCompatActivity).supportActionBar?.title = "Movies"
        homeViewModel.fetchPopularMovies()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d("mytag", "In movie")
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)
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
        adapter.fetchMore = homeViewModel::fetchPopularMovies

    }
}