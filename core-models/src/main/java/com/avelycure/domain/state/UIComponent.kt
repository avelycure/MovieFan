package com.avelycure.domain.state

sealed class UIComponent {
    data class Dialog(
        val description: String,
    ) : UIComponent()
}