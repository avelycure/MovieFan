package com.avelycure.core_navigation

import android.content.Context
import android.os.Bundle
import java.io.Serializable

/**
 * @directory is the part of bottom navigation
 */
interface Navigator : Serializable {
    fun openLastFragmentInDirectory(dir: String)
    fun add(
        directory: String,
        tag: String,
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
    fun onDestroy(outState: Bundle)
    fun recreate(context: Context, finish: () -> Unit, savedInstanceState: Bundle)
}