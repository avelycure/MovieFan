package com.avelycure.core_navigation

import android.os.Bundle

/**
 * @directory is the part of bottom navigation
 */
interface Navigator {
    fun openLastFragmentInDirectory(dir: String)
    fun add(
        directory: String,
        fragmentName: String,
        bundle: Bundle
    )
    fun setHomeFragment()
    fun back()
}