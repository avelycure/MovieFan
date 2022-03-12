package com.avelycure.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.avelycure.core_navigation.*

/**
 * Class to navigate between fragments
 */
class Compas() : Navigator {

    private lateinit var fragmentManager: FragmentManager

    private var containerId: Int = 0

    private lateinit var curDir: String

    var finishApp: () -> Unit = {}

    /**
     * Global directories
     */
    private val fragments: MutableList<DirectoryStack> = mutableListOf()

    /**
     * Objects that create fragments
     */
    private val instantiators: HashMap<String, IInstantiator> = hashMapOf()

    /**
     * If directory is not empty -> check if there is needed fragment
     */
    override fun openLastFragmentInDirectory(dir: String) {
        if (directoryIsNotEmpty(dir)) {
            val fragment = fragments.find { it.dirName == dir }?.fragments?.last()?.fragment

            if (fragment != null) {

                if (dir != curDir)
                    clearBackStack()

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
        tag: String,
        bundle: Bundle
    ) {
        //the directory we are adding to
        //val dirTag = fragments.find { it.dirName == directory }
        val dir = fragments.find { it.dirName == directory }

        val fragment = instantiators[tag]?.getInstance(bundle)

        if (fragment != null) {
            if (directory != curDir)
                clearBackStack()

            dir?.fragments?.add(Screen(directory, tag, fragment))

            fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commit()

            curDir = directory
        }
    }

    private fun clearBackStack() {
        val count = fragmentManager.backStackEntryCount
        for (i in 0 until count) {
            fragmentManager.popBackStack()
        }
    }

    override fun setUpNavigation(
        c: Context,
        rootFragments: List<DirectoryStack>,
        insts: List<IInstantiator>,
        id: Int,
        finish: () -> Unit
    ) {
        for (root in rootFragments)
            fragments.add(DirectoryStack(root.dirName, mutableListOf()))

        for (inst in insts)
            instantiators[inst.getTag()] = inst

        containerId = id
        curDir = fragments[0].dirName
        fragmentManager = (c as AppCompatActivity).supportFragmentManager
        finishApp = finish
    }

    /**
     * If it has 1 or more fragments in stack
     */
    override fun directoryIsNotEmpty(dir: String): Boolean {
        return fragments
            .find { it.dirName == dir }
            ?.fragments
            ?.isNotEmpty() ?: false
    }

    /**
     * If directory has more than one fragment than back to previous,
     * else go to previous directory
     * If the first directory has only root element than closeApp
     */
    override fun back() {
        //current directory
        val dir = fragments.find { it.dirName == curDir }

        //if there is more than one fragment in this directory
        if (dir?.fragments?.size ?: 0 > 1) {

            dir?.fragments?.removeLast()

            openLastFragmentInDirectory(curDir)
        } else {
            // if there is one or less(this must be impossible)

            while (true) {
                val prevDir = fragments.indexOfFirst { it.dirName == curDir } - 1

                // if there are no such directory then -1-1=-2
                // if there are no more fragments then 0-1=-1
                if (prevDir > -1) {
                    curDir = fragments[prevDir].dirName
                    if (directoryIsNotEmpty(curDir)) {
                        openLastFragmentInDirectory(curDir)
                        return
                    }
                } else
                    closeApp()
            }
        }
    }

    private fun closeApp() {
        finishApp()
    }


//Handling restore state


    private fun saveState(outState: Bundle) {
        val dir = fragments.find { it.dirName == curDir }

        val count = dir?.fragments?.size ?: 0

        if (dir != null) {
            for (i in 0 until count) {
                Log.d("mytag", "SAVE" + dir.fragments[i])
                fragmentManager.putFragment(outState, dir.fragments[i].tag, dir.fragments[i].fragment)
            }
        }
    }


    override fun recreate(
        context: Context,
        finish: () -> Unit,
        savedInstanceState: Bundle
    ) {
        fragmentManager = (context as AppCompatActivity).supportFragmentManager
        finishApp = finish

        /*val dir = fragments.find { it.dirName == curDir }
    val tags = fragmentTags.find { it.dirName == curDir }
    if (dir != null && tags != null)
        for (i in 0 until dir.data.size) {
            val f = fragmentManager.getFragment(savedInstanceState, tags.data[i])
            if (f != null)
                dir.data[i] = f
        }*/
    }

    private fun clearOtherBranches() {
        for (dir in fragments) {
            if (dir.dirName != curDir) {
                dir.fragments.clear()
            }
        }
    }

    override fun onDestroy(outState: Bundle) {
        //clearBackStack()
        clearOtherBranches()
        saveState(outState)
    }
}