package com.avelycure.core_navigation

import android.content.Context
import android.os.Bundle

/**
 * @directory is the part of bottom navigation
 */
interface Navigator {
    fun recreate(context: Context, finish: () -> Unit)

    fun openLastFragmentInDirectory(dir: String)
    fun add(
        directory: String,
        fragmentName: String,
        bundle: Bundle
    )

    fun setUpNavigation(
        c: Context,
        rootFragments: List<DirectoryStack>,
        insts: List<IInstantiator>,
        id: Int,
        finish: () -> Unit
    )

    fun back()
    fun directoryIsNotEmpty(dir: String): Boolean
}