package com.avelycure.core_navigation

import androidx.fragment.app.Fragment

data class Screen(
    val directory: String,
    val tag: String,
    val fragment: Fragment
)