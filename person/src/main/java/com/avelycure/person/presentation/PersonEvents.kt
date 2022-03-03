package com.avelycure.person.presentation

sealed class PersonEvents {

    object OnRemoveHeadFromQueue: PersonEvents()

    object OnOpenPersonScreen: PersonEvents()

}