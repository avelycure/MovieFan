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

    fun onTrigger(event: PersonEvents) {
        when (event) {
            is PersonEvents.OnRemoveHeadFromQueue -> {
                //when handled message remove it from queue
                removeHeadMessage()
            }
            is PersonEvents.OnExpandPerson -> {
                onExpand(event.personId, event.itemId)
            }
            is PersonEvents.OnGotTextFlow -> {
                searchPersonByName(event.queryFlow)
            }
            is PersonEvents.OnRequestMoreData -> {
                when (_state.value.mode) {
                    PersonFragmentMode.POPULAR -> {
                        getPopularPerson()
                    }
                    PersonFragmentMode.SEARCH -> {
                        searchMorePersons(lastQuery)
                    }
                }
            }
            is PersonEvents.OnModeEnabled -> {
                //this event is triggered when fragment changes mode
                _state.value = _state.value.copy(mode = event.mode)
            }
        }
    }

    private fun onExpand(personId: Int, itemId: Int) {
        viewModelScope.launch {
            getPersonInfo.execute(personId).collect { dataState ->
                when (dataState) {
                    is DataState.Data -> {
                        //first we need to make new list not to change old
                        val list = _state.value.persons.toMutableList()
                        //second we need to make new person not to change old
                        val updatedPerson = list[itemId].copy().apply {
                            setProperties(dataState.data)
                            expanded = !list[itemId].expanded
                        }
                        list[itemId] = updatedPerson
                        _state.value = _state.value.copy(
                            persons = list
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
    private var lastQuery = ""

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

    // this method is called when we already called searchPersonByName, so we add persons
    // to previously created list
    private fun searchMorePersons(query: String) {
        viewModelScope.launch {
            searchPerson.execute(query, lastVisiblePage).collect { dataState ->
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

    private fun searchPersonByName(queryFlow: Flow<String>) {
        viewModelScope.launch {
            queryFlow
                .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    lastVisiblePage = 1
                    lastQuery = query
                    if (query.isEmpty())
                        getPopularPersons.execute(lastVisiblePage)
                    else
                        searchPerson.execute(query, lastVisiblePage)
                }.collect { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            _state.value = _state.value.copy(
                                persons = dataState.data ?: emptyList()
                            )
                            lastVisiblePage++
                        }
                        is DataState.Response -> {
                            appendToMessageQueue(
                                dataState.uiComponent as UIComponent.Dialog
                            )
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