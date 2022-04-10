package com.avelycure.movie.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.models.Movie
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.Queue
import com.avelycure.domain.state.UIComponent
import com.avelycure.movie.domain.interactors.GetPopularMovies
import com.avelycure.movie.domain.interactors.SearchMovie
import com.avelycure.movie.presentation.HomeFragmentMode.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val getPopularMovies: GetPopularMovies,
    private val searchMovie: SearchMovie
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    private val TAG = "HomeViewModel"

    fun onTrigger(event: HomeEvents) {
        when (event) {
            is HomeEvents.OnRemoveHeadFromQueue -> {
                //when handled message remove it from queue
                removeHeadMessage()
            }
            is HomeEvents.OnGotTextFlow -> {
                searchMovieByTitle(event.queryFlow)
            }
            is HomeEvents.OnRequestMoreData -> {
                when (_state.value.mode) {
                    POPULAR -> {
                        fetchPopularMovies()
                    }
                    SEARCH -> {
                        searchMoreMovies(lastQuery)
                    }
                }
            }
            is HomeEvents.OnModeEnabled -> {
                //this event is triggered when fragment changes mode
                _state.value = _state.value.copy(mode = event.mode)
            }
        }
    }

    private var lastQuery = ""
    private var lastVisiblePage = 1

    private fun searchMovieByTitle(queryFlow: Flow<String>) {
        viewModelScope.launch {
            queryFlow
                .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    lastVisiblePage = 1
                    lastQuery = query
                    if (query.isEmpty())
                        getPopularMovies.execute(lastVisiblePage)
                    else
                        searchMovie.execute(query, lastVisiblePage)
                }.collect { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            _state.value = _state.value.copy(
                                movies = dataState.data ?: emptyList()
                            )
                            lastVisiblePage++
                        }
                        is DataState.Response -> {
                            appendToMessageQueue(dataState.uiComponent)
                        }
                        is DataState.Loading -> {
                            _state.value =
                                _state.value.copy(progressBarState = dataState.progressBarState)
                        }
                    }
                }
        }
    }

    private fun fetchPopularMovies() {
            viewModelScope.launch {
                getPopularMovies
                    .execute(lastVisiblePage)
                    .collect { dataState ->
                        when (dataState) {
                            is DataState.Data -> {
                                _state.value = _state.value.copy(
                                    movies = _state.value.movies + (dataState.data ?: emptyList())
                                )
                                lastVisiblePage++
                            }
                            is DataState.Response -> {
                                appendToMessageQueue(dataState.uiComponent)
                            }
                            is DataState.Loading -> {
                                _state.value =
                                    _state.value.copy(progressBarState = dataState.progressBarState)
                            }
                        }
                    }
        }
    }

    private fun searchMoreMovies(query: String) {
        viewModelScope.launch {
            searchMovie
                .execute(query, lastVisiblePage)
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            _state.value = _state.value.copy(
                                movies = _state.value.movies + (dataState.data ?: emptyList()),
                            )
                            lastVisiblePage++
                        }
                        is DataState.Response -> {
                            appendToMessageQueue(dataState.uiComponent)
                        }
                        is DataState.Loading -> {
                            _state.value =
                                _state.value.copy(progressBarState = dataState.progressBarState)
                        }
                    }
                }
        }
    }

    private fun appendToMessageQueue(uiComponent: UIComponent) {
        val queue: Queue<UIComponent> = Queue(mutableListOf())
        for (i in 0 until _state.value.errorQueue.count())
            _state.value.errorQueue.poll()?.let { queue.add(it) }
        queue.add(uiComponent)
        _state.value = _state.value.copy(errorQueue = queue)
    }

    private fun removeHeadMessage() {
        try {
            val queue = _state.value.errorQueue
            queue.remove()
            _state.value = _state.value.copy(errorQueue = queue)
        } catch (e: Exception) {
            Log.d(TAG, "Nothing to remove from MessageQueue")
        }
    }

}