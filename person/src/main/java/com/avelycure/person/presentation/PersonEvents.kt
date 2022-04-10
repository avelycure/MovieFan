package com.avelycure.person.presentation

import kotlinx.coroutines.flow.Flow

enum class PersonFragmentMode{
    POPULAR, SEARCH
}

sealed class PersonEvents {
    object OnRemoveHeadFromQueue : PersonEvents()

    object OnRequestMoreData : PersonEvents()

    data class OnModeEnabled(val mode: PersonFragmentMode): PersonEvents()

    data class OnGotTextFlow(val queryFlow: Flow<String>) : PersonEvents()

    data class OnExpandPerson(val personId: Int, val itemId: Int) : PersonEvents()
}