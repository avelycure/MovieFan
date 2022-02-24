package com.avelycure.movie_info.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.state.DataState
import com.avelycure.movie_info.domain.interactors.GetMovieInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieInfoViewModel
@Inject constructor(
    private val getMovieInfo: GetMovieInfo
) : ViewModel() {
    private val _state = MutableStateFlow(MovieInfoState())
    val state = _state.asStateFlow()

    fun getDetails(id: Int) {
        viewModelScope.launch {
            getMovieInfo.execute(id)
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            _state.value = _state.value.copy(
                                movieInfo = dataState.data ?: _state.value.movieInfo
                            )
                        }
                        is DataState.Loading -> {
                            _state.value =
                                _state.value.copy(progressBarState = dataState.progressBarState)
                        }
                        is DataState.Response -> {
                        }
                    }
                }
        }
    }
}