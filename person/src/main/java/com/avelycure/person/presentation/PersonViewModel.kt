package com.avelycure.person.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.Queue
import com.avelycure.domain.state.UIComponent
import com.avelycure.person.domain.interactors.GetPersonInfo
import com.avelycure.person.domain.interactors.GetPopularPersons
import com.avelycure.person.domain.interactors.SearchPerson
import com.avelycure.person.utils.setProperties
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel
@Inject constructor(
    private val getPopularPersons: GetPopularPersons,
    private val getPersonInfo: GetPersonInfo,
    private val searchPerson: SearchPerson
) : ViewModel() {
    private val TAG = "PersonViewModel"

    private val _state = MutableStateFlow(PersonState())
    val state = _state.asStateFlow()

    private var query = flow<String> { }

    fun onTrigger(event: PersonEvents) {
        when (event) {
            is PersonEvents.OnRemoveHeadFromQueue -> removeHeadMessage()
            is PersonEvents.OnRequestPopularPerson -> {
                _state.value = _state.value.copy(mode = "popular")
                getPopularPerson()
            }
            is PersonEvents.OnExpandPerson -> {
                onExpand(event.personId, event.itemId)
            }
            is PersonEvents.OnSearchPerson -> {
                query = event.queryFlow
                searchPersonByName(query)
            }
            is PersonEvents.OnRequestMoreData -> {
                when (_state.value.mode) {
                    "popular" -> {
                        getPopularPerson()
                    }
                    "search" -> {
                        searchPersonByName(query)
                    }
                }
            }
        }
    }

    private fun onExpand(personId: Int, itemId: Int) {
        viewModelScope.launch {
            getPersonInfo.execute(personId).collect { dataState ->
                when (dataState) {
                    is DataState.Data -> {
                        val list = _state.value.persons.toMutableList()
                        val updatedPerson = list[itemId].copy()
                        updatedPerson.setProperties(dataState.data)
                        updatedPerson.expanded = !list[itemId].expanded
                        list[itemId] = updatedPerson
                        _state.value = _state.value.copy(
                            persons = list,
                            lastExpandedItem = itemId
                        )
                    }
                    is DataState.Response -> {
                        appendToMessageQueue(
                            dataState.uiComponent as UIComponent.Dialog
                        )
                    }
                    is DataState.Loading -> {
                        _state.value = _state.value.copy(
                            progressBarState = dataState.progressBarState
                        )
                    }
                }
            }
        }
    }

    private var lastVisiblePage = 1

    private fun getPopularPerson() {
        viewModelScope.launch {
            getPopularPersons.execute(lastVisiblePage).collect { dataState ->
                when (dataState) {
                    is DataState.Data -> {
                        _state.value = _state.value.copy(
                            persons = _state.value.persons + (dataState.data ?: emptyList()),
                        )
                        lastVisiblePage++
                    }
                    is DataState.Loading -> {
                        _state.value = _state.value.copy(
                            progressBarState = dataState.progressBarState
                        )
                    }
                    is DataState.Response -> {
                        appendToMessageQueue(
                            dataState.uiComponent as UIComponent.Dialog
                        )
                    }
                }
            }
        }
    }

    //todo add clear previous persons from list
    private fun searchPersonByName(queryFlow: Flow<String>) {
        viewModelScope.launch {
            queryFlow
                .debounce(500)
                .filter { query ->
                    return@filter query.isNotEmpty()
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    Log.d("mytag", "requested")
                    _state.value = _state.value.copy(mode = "search")
                    lastVisiblePage = 1
                    searchPerson.execute(query, lastVisiblePage)
                }.collect { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            _state.value = _state.value.copy(
                                persons = _state.value.persons + (dataState.data ?: emptyList()),
                            )
                            lastVisiblePage++
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