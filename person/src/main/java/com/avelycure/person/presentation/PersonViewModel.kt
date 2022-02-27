package com.avelycure.person.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.state.DataState
import com.avelycure.person.domain.interactors.GetPopularPersons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel
@Inject constructor(
    private val getPopularPersons: GetPopularPersons
) : ViewModel() {

    private val _state = MutableStateFlow(PersonState())
    val state = _state.asStateFlow()

    fun getPopularPerson(page: Int) {
        viewModelScope.launch {
            getPopularPersons.execute(page).collect { dataState ->
                when (dataState) {
                    is DataState.Data -> {
                        _state.value = _state.value.copy(
                            persons = _state.value.persons + (dataState.data?: emptyList()),
                            lastVisiblePage = _state.value.lastVisiblePage + 1
                        )
                    }
                    is DataState.Loading -> {
                        _state.value = _state.value.copy(
                            progressBarState = dataState.progressBarState
                        )
                    }
                    is DataState.Response -> {
                    }
                }
            }
        }
    }
}