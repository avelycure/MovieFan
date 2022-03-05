package com.example.office.presentation

sealed class OfficeEvents{
    object OnRemoveHeadFromQueue: OfficeEvents()

    data class OnOpenInfoFragment(
        val movieId: Int
    ): OfficeEvents()

}