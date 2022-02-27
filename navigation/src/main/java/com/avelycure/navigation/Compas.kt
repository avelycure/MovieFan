package com.avelycure.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.avelycure.core_navigation.DirectoryStack
import com.avelycure.core_navigation.IInstantiator
import com.avelycure.core_navigation.Navigator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Class to navigate between fragments
 */
class Compas() : Navigator {

    private lateinit var context: Context

    private lateinit var fragmentManager: FragmentManager

    private var containerId: Int = 0

    private lateinit var curDir: String

    var finishApp: () -> Unit = {}

    /**
     * Global directories
     */
    private val roots: MutableList<DirectoryStack> = mutableListOf()

    /**
     * Objects that create fragments
     */
    private val instantiators: HashMap<String, IInstantiator> = hashMapOf()

    /**
     * If directory is not empty -> check if there is needed fragment
     */
    override fun openLastFragmentInDirectory(dir: String) {
        if (directoryIsNotEmpty(dir)) {
            val fragment = roots.find { it.dirName == dir }?.data?.last()
            if (fragment != null) {
                fragmentManager
                    .beginTransaction()
                    .replace(containerId, fragment)
                    .commit()
                curDir = dir
            }
        }
    }

    override fun add(
        directory: String,
        fragmentName: String,
        bundle: Bundle
    ) {
        val fragment = instantiators[fragmentName]?.getInstance(bundle)
        if (fragment != null) {
            //todo add some checks
            roots.find { it.dirName == directory }?.data?.add(fragment)

            fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commit()

            curDir = directory
        }
    }


    override fun setHomeFragment(
        c: Context,
        rootFragments: List<DirectoryStack>,
        insts: List<IInstantiator>,
        id: Int,
        finish: () -> Unit
    ) {
        for (root in rootFragments)
            roots.add(root)

        for (inst in insts)
            instantiators[inst.getTag()] = inst

        containerId = id
        context = c
        fragmentManager = (context as AppCompatActivity).supportFragmentManager
        finishApp = finish

        val fragment = instantiators[roots[0].dirName]?.getInstance(Bundle())

        if (fragment != null) {
            curDir = roots[0].dirName
            fragmentManager.beginTransaction()
                .add(containerId, fragment)
                .commit()
        }
    }

    /**
     * If it has 1 or more fragments in stack
     */
    private fun directoryIsNotEmpty(dir: String): Boolean {
        return roots
            .find { it.dirName == dir }
            ?.data
            ?.isNotEmpty() ?: false
    }

    override fun back() {
        if (roots.find { it.dirName == curDir }?.data?.size != 1) {
            roots.find { it.dirName == curDir }?.data?.removeLast()
            openLastFragmentInDirectory(curDir)
            Log.d("mytag", "Called back1")
        } else {
            Log.d("mytag", "Called back2")
            val prevDir = roots.indexOfFirst { it.dirName == curDir } - 1
            if (prevDir != -1) {
                curDir = roots[prevDir].dirName
                back()
            } else {
                Log.d("mytag", "Called back3")
                closeApp()
            }
        }
    }

    private fun closeApp() {
        finishApp()
    }
}