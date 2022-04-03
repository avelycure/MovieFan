package com.avelycure.person.presentation

sealed class PersonEvents {
    object OnRemoveHeadFromQueue: PersonEvents()

    object OnRequestMorePersons: PersonEvents()

    data class OnExpandPerson(val personId: Int, val itemId: Int): PersonEvents()
}