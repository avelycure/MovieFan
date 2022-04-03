package com.avelycure.movie.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.state.DataState
import com.avelycure.movie.domain.interactors.GetPopularMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val getPopularMovies: GetPopularMovies
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private lateinit var job: Job

    fun fetchPopularMovies() {
        if (!this::job.isInitialized || !job.isActive) {
            job = viewModelScope.launch {
                getPopularMovies
                    .execute(state.value.lastVisiblePage)
                    .collect { dataState ->
                        when (dataState) {
                            is DataState.Data -> {
                                _state.value = _state.value.copy(
                                    movies = _state.value.movies + (dataState.data ?: emptyList()),
                                    lastVisiblePage = _state.value.lastVisiblePage + 1
                                )
                            }
                            is DataState.Response -> {
                                Log.d("mytag", "no internet exception in vm")
                            }
                            is DataState.Loading -> {
                                _state.value =
                                    _state.value.copy(progressBarState = dataState.progressBarState)
                            }
                        }
                    }
            }
        }
    }

}