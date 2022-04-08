package com.avelycure.person.presentation

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.core_navigation.IInstantiator
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.image_loader.ImageLoader
import com.avelycure.person.R
import com.avelycure.person.presentation.adapters.PersonAdapter
import com.avelycure.person.utils.PersonDiffutilsCallback
import com.avelycure.resources.getQueryChangeStateFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class PersonFragment : Fragment() {
    companion object Instantiator : IInstantiator {
        private const val tag = "PERSON"
        override fun getTag(): String {
            return tag
        }

        override fun getInstance(bundle: Bundle): Fragment {
            val movieInfoFragment = PersonFragment()
            movieInfoFragment.arguments = bundle
            return movieInfoFragment
        }
    }

    private val personViewModel: PersonViewModel by viewModels()

    private lateinit var rvPersons: RecyclerView
    private lateinit var personAdapter: PersonAdapter

    private lateinit var searchView: SearchView

    private lateinit var pb: ProgressBar

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.person_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView(view)

        (activity as AppCompatActivity).supportActionBar?.title = "Persons"

        pb = view.findViewById(R.id.p_progress_bar)

        personViewModel.onTrigger(PersonEvents.OnRequestPopularPerson)

        lifecycleScope.launchWhenStarted {
            personViewModel.state.collect { state ->
                val diffutilsCallback =
                    PersonDiffutilsCallback(personAdapter.data, state.persons)
                val diffUtilResult = DiffUtil.calculateDiff(diffutilsCallback)
                personAdapter.data.clear()
                personAdapter.data.addAll(state.persons)
                diffUtilResult.dispatchUpdatesTo(personAdapter)

                if (state.progressBarState is ProgressBarState.Loading)
                    pb.visibility = View.VISIBLE
                else {
                    pb.visibility = View.GONE
                }
                personAdapter.loading = false
            }
        }
    }

    private fun setRecyclerView(view: View) {
        personAdapter = PersonAdapter()
        personAdapter.scope = lifecycleScope
        personAdapter.loadImage = { url, iv -> imageLoader.loadImage(url, iv) }
        personAdapter.onExpand = { personId, itemId ->
            personViewModel.onTrigger(PersonEvents.OnExpandPerson(personId, itemId))
        }
        personAdapter.fetchMore = {
            personViewModel.onTrigger(PersonEvents.OnRequestMoreData)
        }

        rvPersons = view.findViewById(R.id.p_recycler_view)
        rvPersons.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvPersons.adapter = personAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.persons_menu, menu)
        initSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initSearchView(menu: Menu) {
        val searchManager =
            (activity as AppCompatActivity).getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search_view).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo((activity as AppCompatActivity).componentName))
        searchView.setIconifiedByDefault(false)
        personViewModel.onTrigger(PersonEvents.OnSearchPerson(searchView.getQueryChangeStateFlow()))

        // The default state of the homeFragment is showing popular movies, so when we close
        // searchView we are calling fetchPopularMovies()
        (menu.findItem(R.id.search_view) as MenuItem).setOnActionExpandListener(object :
            MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                (activity as AppCompatActivity).invalidateOptionsMenu()
                personViewModel.onTrigger(PersonEvents.OnRequestPopularPerson)
                return true
            }
        })
    }
}