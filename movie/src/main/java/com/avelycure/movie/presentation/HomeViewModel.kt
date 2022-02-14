package com.avelycure.movie.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.models.Movie
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.UIComponent
import com.avelycure.movie.domain.interactors.GetPopularMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    val state: MutableState<HomeState> = mutableStateOf(HomeState())

    fun fetchPopularMovies(nextPage: Int) {
        Log.d("mytag", "I got data1")
        viewModelScope.launch {
            getPopularMovies
                .execute(nextPage)
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            Log.d("mytag", "I got data2" + dataState.data?.toString())

                            Log.d("mytag", "Before: " + state.value)
                            state.value = state.value.copy(movies = dataState.data ?: emptyList())
                            Log.d("mytag", "After: " + state.value)

                        }
                        is DataState.Response -> {
                            Log.d(
                                "mytag",
                                "I got response" + (dataState.uiComponent as UIComponent.Dialog).description
                            )
                        }
                        is DataState.Loading -> {
                            Log.d("mytag", "I got loading")
                            state.value =
                                state.value.copy(progressBarState = dataState.progressBarState)
                        }
                    }
                }

        }
    }

}