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

    fun fetchPopularMovies(nextPage: Int) {
        viewModelScope.launch {
            getPopularMovies
                .execute(nextPage)
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            state.value = state.value.copy(movies = dataState.data ?: emptyList())
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