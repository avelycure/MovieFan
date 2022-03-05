package com.avelycure.person.presentation

sealed class PersonEvents {

    object OnRemoveHeadFromQueue: PersonEvents()

    object OnOpenPersonScreen: PersonEvents()

    data class OnExpandPerson(val personId: Int, val itemId: Int): PersonEvents()

}