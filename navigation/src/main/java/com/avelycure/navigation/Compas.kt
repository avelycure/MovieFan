package com.avelycure.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
        Log.d("mytag", "open: ${fragmentManager.fragments}")
        Log.d("mytag", "open: ${fragments}")
        if (directoryIsNotEmpty(dir)) {
            val fragment = fragments.find { it.dirName == dir }?.fragments?.last()?.fragment

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
        tag: String,
        bundle: Bundle
    ) {
        //the directory we are adding to
        //val dirTag = fragments.find { it.dirName == directory }
        val dir = fragments.find { it.dirName == directory }

        val fragment = instantiators[tag]?.getInstance(bundle)

        if (fragment != null) {

            dir?.fragments?.add(Screen(directory, tag, fragment))

            fragmentManager.beginTransaction()
                .add(containerId, fragment)
                .addToBackStack(tag)
                .commit()

            curDir = directory
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
        Log.d("mytag", "Fragments1: ${fragments}")
        Log.d("mytag", "Fragments1: ${fragmentManager.fragments}")
        //current directory
        val dir = fragments.find { it.dirName == curDir }

        //if there is more than one fragment in this directory
        if (dir?.fragments?.size ?: 0 > 1) {

            dir?.fragments?.removeLast()

            //immediate?
            fragmentManager.popBackStack()
            Log.d("mytag", "Fragments2: ${fragments}")
            Log.d("mytag", "Fragments2: ${fragmentManager.fragments}")

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

    private fun saveState(outState: Bundle) {

        for (i in 0 until fragments.size) {

            val count = fragments[i].fragments.size

            for (j in 0 until count) {
                fragmentManager.putFragment(
                    outState,
                    (i * fragments.size + j).toString(),
                    fragments[i].fragments[j].fragment
                )
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

        Log.d("mytag", "When recreating: " + fragmentManager.fragments)

        for (i in 0 until fragments.size) {

            for (j in 0 until fragments[i].fragments.size) {
                val f = fragmentManager.getFragment(
                    savedInstanceState,
                    (i * fragments.size + j).toString()
                )
                if (f != null)
                    fragments[i].fragments[j].fragment = f
            }
        }
    }

    /*private fun saveState(outState: Bundle) {
        val dir = fragments.find { it.dirName == curDir }
        val count = dir?.fragments?.size ?: 0

        if (dir != null) {
            for (i in 0 until count) {
                fragmentManager.putFragment(
                    outState,
                    i.toString(),
                    dir.fragments[i].fragment
                )
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

        //only in this version of save state
        clearOtherBranches()

        Log.d("mytag", "When recreating: " + fragmentManager.fragments)

        val dir = fragments.find { it.dirName == curDir }

        if (dir != null)
            for (i in 0 until dir.fragments.size) {
                val f = fragmentManager.getFragment(savedInstanceState, i.toString())
                if (f != null)
                    dir.fragments[i].fragment = f
            }
    }

    private fun clearOtherBranches() {
        for (dir in fragments) {
            if (dir.dirName != curDir) {
                for (screen in dir.fragments) {
                    fragmentManager.beginTransaction()
                        .remove(screen.fragment)
                        .commit()
                }
            }
        }
    }*/

    override fun onDestroy(outState: Bundle) {
        Log.d("mytag", "Before destroy: " + fragmentManager.fragments)
        saveState(outState)
    }
}