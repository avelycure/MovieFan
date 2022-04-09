package com.avelycure.person.presentation

import kotlinx.coroutines.flow.Flow

sealed class PersonEvents {
    object OnRemoveHeadFromQueue : PersonEvents()

    object OnRequestMoreData : PersonEvents()

    object OnRequestPopularPerson : PersonEvents()

    object OnSearchModeEnabled: PersonEvents()
    object OnDefaultModeEnabled: PersonEvents()

    data class OnSearchPerson(val queryFlow: Flow<String>) : PersonEvents()

    data class OnExpandPerson(val personId: Int, val itemId: Int) : PersonEvents()
}