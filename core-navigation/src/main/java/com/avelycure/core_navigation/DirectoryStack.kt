package com.avelycure.core_navigation

import androidx.fragment.app.Fragment

data class DirectoryStack(
    val dirName: String,
    val data: MutableList<Fragment>
)