package com.avelycure.person.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.state.DataState
import com.avelycure.person.domain.interactors.GetPopularPersons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel
@Inject constructor(
    private val getPopularPersons: GetPopularPersons
) : ViewModel() {

    val state: MutableState<PersonState> = mutableStateOf(PersonState())

    fun getPopularPerson(page: Int) {
        viewModelScope.launch {
            getPopularPersons.execute(page).collect { dataState ->
                when (dataState) {
                    is DataState.Data -> {
                        state.value = state.value.copy(
                            persons = state.value.persons + (dataState.data?: emptyList()),
                            lastVisiblePage = state.value.lastVisiblePage + 1
                        )
                    }
                    is DataState.Loading -> {
                    }
                    is DataState.Response -> {
                    }
                }
            }
        }
    }
}