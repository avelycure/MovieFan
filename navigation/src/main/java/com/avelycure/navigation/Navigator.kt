package com.avelycure.navigation

import androidx.fragment.app.Fragment

/**
 * @directory is the part of bottom navigation
 */
interface Navigator {
    fun add(directory: String, tag: String, fragment: Fragment)
    fun back(currentDirectory: String)
}