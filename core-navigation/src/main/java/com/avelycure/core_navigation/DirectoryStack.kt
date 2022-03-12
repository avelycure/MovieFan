package com.avelycure.core_navigation

data class DirectoryStack(
    val dirName: String,
    val fragments: MutableList<Screen>
)