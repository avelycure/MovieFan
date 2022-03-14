package com.avelycure.person.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.core_navigation.IInstantiator
import com.avelycure.core_navigation.NavigationConstants
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.person.R
import com.avelycure.person.presentation.adapters.PersonAdapter
import com.avelycure.person.presentation.adapters.PersonImagesAdapter
import com.avelycure.person.utils.PersonDiffutilsCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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

    private lateinit var pb: ProgressBar

    private lateinit var loadImages: (String, ImageView) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.person_fragment, container, false)

        loadImages =
            arguments?.getSerializable(NavigationConstants.LOAD_IMAGES) as? (String, ImageView) -> Unit
                ?: { _, _ -> }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView(view)

        pb = view.findViewById(R.id.p_progress_bar)

        personViewModel.onTrigger(PersonEvents.OnOpenPersonScreen)

        lifecycleScope.launchWhenStarted {
            personViewModel.state.collect { state ->
                val prevSize = personAdapter.data.size
                personAdapter.data = state.persons
                if (prevSize != state.persons.size)
                    personAdapter.notifyItemRangeInserted(prevSize, 15)
                personAdapter.notifyItemChanged(state.lastExpandedItem)

                if (state.progressBarState is ProgressBarState.Loading)
                    pb.visibility = View.VISIBLE
                else
                    pb.visibility = View.GONE
            }
        }
    }

    private fun setRecyclerView(view: View) {
        personAdapter = PersonAdapter()
        personAdapter.scope = lifecycleScope
        personAdapter.loadImage = loadImages
        personAdapter.onExpand = { personId, itemId ->
            personViewModel.onTrigger(PersonEvents.OnExpandPerson(personId, itemId))
        }

        rvPersons = view.findViewById(R.id.p_recycler_view)
        rvPersons.adapter = personAdapter
        rvPersons.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}