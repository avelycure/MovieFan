package com.avelycure.movie.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.state.DataState
import com.avelycure.movie.domain.interactors.GetPopularMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val getPopularMovies: GetPopularMovies
) : ViewModel() {
    val state: MutableState<HomeState> = mutableStateOf(HomeState())

    fun fetchPopularMovies() {
        viewModelScope.launch {
            getPopularMovies
                .execute(state.value.lastVisiblePage)
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            state.value = state.value.copy(
                                movies = state.value.movies + (dataState.data ?: emptyList()),
                                lastVisiblePage = state.value.lastVisiblePage + 1
                            )
                        }
                        is DataState.Response -> {
                        }
                        is DataState.Loading -> {
                            state.value =
                                state.value.copy(progressBarState = dataState.progressBarState)
                        }
                    }
                }
        }
    }

}