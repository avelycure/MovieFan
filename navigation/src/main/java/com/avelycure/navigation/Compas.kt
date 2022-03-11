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
    private val fragmentTags: MutableList<DirStack> = mutableListOf()

    /**
     * Objects that create fragments
     */
    private val instantiators: HashMap<String, IInstantiator> = hashMapOf()

    /**
     * If directory is not empty -> check if there is needed fragment
     */
    override fun openLastFragmentInDirectory(dir: String) {
        if (directoryIsNotEmpty(dir)) {
            val fragment = fragments.find { it.dirName == dir }?.data?.last()
            if (fragment != null) {
                fragmentManager
                    .beginTransaction()
                    .replace(containerId, fragment)
                    .commit()
                curDir = dir
            }
        }
    }

    override fun recreate(
        context: Context,
        finish: () -> Unit
    ) {
        fragmentManager = (context as AppCompatActivity).supportFragmentManager
        finishApp = finish
    }

    fun onDestroy() {

    }

    override fun add(
        directory: String,
        fragmentName: String,
        bundle: Bundle
    ) {
        //the directory we are adding to
        val dirTag = fragmentTags.find { it.dirName == directory }
        val dirFrag = fragments.find { it.dirName == directory }

        Log.d("mytag", "TUT1")
        Log.d("mytag", "TUT1: " + dirTag?.data?.isEmpty())
        Log.d("mytag", "TUT1")

        //There are two ways:
        // 1. the directory is empty -> create fragment(it will be root fragment)
        // 2. the directory is not empty:
        //                              a) the only fragment is root
        //                              b) there are other fragments also

        // if the name of first fragment in directory exists then the directory is not empty

        if (dirTag?.data?.isNotEmpty() == true) {
            if (dirTag.data[0] == fragmentName) {
                //if the added fragment is root fragment
                val fragment = dirFrag?.data?.get(0)
                Log.d("mytag", "FM1: $fragment")
                if (fragment != null) {
                    fragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commit()

                    curDir = directory
                }
            } else {
                Log.d("mytag", "TUT2")
                val fragment = instantiators[fragmentName]?.getInstance(bundle)

                Log.d("mytag", "FM2: $fragment")

                if (fragment != null) {
                    dirTag.data.add(fragmentName)

                    dirFrag?.data?.add(fragment)

                    fragmentManager.beginTransaction()
                        .replace(containerId, fragment, fragmentName)
                        .commit()

                    curDir = directory
                }
            }
        } else {
            Log.d("mytag", "TUT2")
            val fragment = instantiators[fragmentName]?.getInstance(bundle)

            Log.d("mytag", "FM2: $fragment")

            if (fragment != null) {
                dirTag?.data?.add(fragmentName)
                dirFrag?.data?.add(fragment)

                fragmentManager.beginTransaction()
                    .replace(containerId, fragment, fragmentName)
                    .commit()

                curDir = directory
            }
        }

        Log.d("mytag", "FM3: " + fragmentManager.fragments)
        Log.d("mytag", "FM3: " + dirTag?.data)
    }

    override fun setUpNavigation(
        c: Context,
        rootFragments: List<DirectoryStack>,
        insts: List<IInstantiator>,
        id: Int,
        finish: () -> Unit
    ) {
        for (root in rootFragments) {
            fragments.add(DirectoryStack(root.dirName, mutableListOf()))
            fragmentTags.add(DirStack(root.dirName, mutableListOf()))
        }

        for (inst in insts)
            instantiators[inst.getTag()] = inst

        containerId = id
        fragmentManager = (c as AppCompatActivity).supportFragmentManager
        finishApp = finish
    }

    /**
     * If it has 1 or more fragments in stack
     */
    override fun directoryIsNotEmpty(dir: String): Boolean {
        return fragmentTags
            .find { it.dirName == dir }
            ?.data
            ?.isNotEmpty() ?: false
    }

    /**
     * If directory has more than one fragment than back to previous,
     * else go to previous directory
     * If the first directory has only root element than closeApp
     */
    override fun back() {
        //current directory
        val dirTag = fragmentTags.find { it.dirName == curDir }
        val dirFrag = fragments.find { it.dirName == curDir }

        //if there is more than one fragment in this directory
        if (dirTag?.data?.size ?: 0 > 1) {

            dirTag?.data?.removeLast()
            dirFrag?.data?.removeLast()

            openLastFragmentInDirectory(curDir)
        } else {
            // if there is one or less(this must be impossible)

            while (true) {
                val prevDir = fragmentTags.indexOfFirst { it.dirName == curDir } - 1

                // if there are no such directory then -1-1=-2
                // if there are no more fragments then 0-1=-1
                if (prevDir > -1) {
                    curDir = fragmentTags[prevDir].dirName
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
}