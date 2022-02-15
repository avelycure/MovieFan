package com.avelycure.movie_info.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.models.MovieInfo
import com.avelycure.domain.state.DataState
import com.avelycure.movie_info.domain.interactors.GetMovieInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieInfoViewModel
@Inject constructor(
    private val getMovieInfo: GetMovieInfo
) : ViewModel() {
    val state: MutableState<MovieInfoState> = mutableStateOf(MovieInfoState())

    fun getDetails(id: Int) {
        viewModelScope.launch {
            getMovieInfo.execute(id)
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            state.value = state.value.copy(
                                movieInfo = dataState.data ?: state.value.movieInfo
                            )
                        }
                        is DataState.Loading -> {
                            state.value =
                                state.value.copy(progressBarState = dataState.progressBarState)
                        }
                        is DataState.Response -> {
                        }
                    }
                }
        }
    }
}