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
    //private val roots: MutableList<DirectoryStack> = mutableListOf()

    private val rootTag: MutableList<DirStack> = mutableListOf()
    private val isFirstTimeOpened: MutableList<Directory> = mutableListOf()

    /**
     * Objects that create fragments
     */
    private val instantiators: HashMap<String, IInstantiator> = hashMapOf()

    /**
     * If directory is not empty -> check if there is needed fragment
     */
    override fun openLastFragmentInDirectory(dir: String) {
        if (directoryIsNotEmpty(dir)) {
            val tag = rootTag.find { it.dirName == dir }?.data?.last()
            val fragment = fragmentManager.findFragmentByTag(tag)
            if (fragment != null) {
                fragmentManager
                    .beginTransaction()
                    .replace(containerId, fragment)
                    .commit()
                curDir = dir
            }
        }
    }

    /**
     * If there is instantiator create fragment with its instantiator but not add it to container,
     * else do nothing
     */
    override fun prepareRoot(
        directory: String,
        fragmentName: String,
        bundle: Bundle
    ) {
        /*val fragment = instantiators[fragmentName]?.getInstance(bundle)
        if (fragment != null) {
            roots.find { it.dirName == directory }?.data?.add(fragment)
        }*/
    }

    override fun recreate(
        context: Context,
        finish: () -> Unit
    ) {
        fragmentManager = (context as AppCompatActivity).supportFragmentManager
        finishApp = finish
        /*val fragments = fragmentManager.fragments.toMutableList()

        for (root in roots) {
            for (i in 0 until root.data.size) {
                val newFragment = fragments.find { it.tag == root.data[i].tag }
                val fragmentIndex = fragments.indexOf(newFragment)

                if (newFragment != null) {
                    root.data[i] = newFragment
                    fragments.removeAt(fragmentIndex)
                }
            }
        }*/
    }

    fun onDestroy() {

    }

    /**
     * If there is instantiator add fragment to container
     *
     * old
     */
    /*override fun add(
        directory: String,
        fragmentName: String,
        bundle: Bundle
    ) {
        val fragment = instantiators[fragmentName]?.getInstance(bundle)
        val tag = instantiators[fragmentName]?.getTag()

        if (fragment != null) {
            roots.find { it.dirName == directory }?.data?.add(fragment)

            fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(tag)
                .commit()

            curDir = directory
        }
    }*/

    override fun add(
        directory: String,
        fragmentName: String,
        bundle: Bundle
    ) {
        //the directory we are adding to
        val dir = rootTag.find { it.dirName == directory }

        Log.d("mytag","TUT1")
        Log.d("mytag","TUT1: " + dir?.data?.isEmpty())
        Log.d("mytag","TUT1")

        //There are two ways:
        // 1. the directory is empty -> create fragment(it will be root fragment)
        // 2. the directory is not empty:
        //                              a) the only fragment is root
        //                              b) there are other fragments also

        // if the name of first fragment in directory exists then the directory is not empty

        if(dir?.data?.isNotEmpty() == true){
            if (dir.data.get(0) ?: "NO_FRAGMENT" == fragmentName) {
                //if the added fragment is root fragment
                val fragment = fragmentManager.findFragmentByTag(fragmentName)
                Log.d("mytag", "FM1: $fragment")
                if (fragment != null) {
                    fragmentManager.beginTransaction()
                        .add(containerId, fragment)
                        .commit()

                    curDir = directory
                }
            }else{
                Log.d("mytag","TUT2")
                val fragment = instantiators[fragmentName]?.getInstance(bundle)

                Log.d("mytag", "FM2: $fragment")

                if (fragment != null) {
                    dir.data.add(fragmentName)

                    fragmentManager.beginTransaction()
                        .add(containerId, fragment, fragmentName)
                        .addToBackStack(fragmentName)
                        .commit()

                    curDir = directory
                }
            }
        }else{
            Log.d("mytag","TUT2")
            val fragment = instantiators[fragmentName]?.getInstance(bundle)

            Log.d("mytag", "FM2: $fragment")

            if (fragment != null) {
                dir?.data?.add(fragmentName)

                fragmentManager.beginTransaction()
                    .add(containerId, fragment, fragmentName)
                    .addToBackStack(fragmentName)
                    .commit()

                curDir = directory
            }
        }

        Log.d("mytag", "FM3: " + fragmentManager.fragments)
        Log.d("mytag", "FM3: " + dir?.data)
    }

    override fun setUpNavigation(
        c: Context,
        rootFragments: List<DirectoryStack>,
        insts: List<IInstantiator>,
        id: Int,
        finish: () -> Unit
    ) {
        for (root in rootFragments) {
            //root.add(DirStack(root.dirName, mutableListOf()))
            rootTag.add(DirStack(root.dirName, mutableListOf()))
            isFirstTimeOpened.add(Directory(root.dirName, true))
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
        return rootTag
            .find { it.dirName == dir }
            ?.data
            ?.isNotEmpty() ?: false
    }

    /**
     * If directory has more than one fragment than back to previous,
     * else go to previous directory
     * If the first directory has only root element than closeApp
     */
    /*override fun back() {
        if (roots.find { it.dirName == curDir }?.data?.size != 1) {
            roots.find { it.dirName == curDir }?.data?.removeLast()
            openLastFragmentInDirectory(curDir)
        } else {
            val prevDir = roots.indexOfFirst { it.dirName == curDir } - 1
            if (prevDir != -1) {
                curDir = roots[prevDir].dirName
                back()
            } else {
                closeApp()
            }
        }
    }*/

    override fun back() {

        //current directory
        val dir = rootTag.find { it.dirName == curDir }

        //if there is more than one fragment in this directory
        if (dir?.data?.size ?: 0 > 1) {

            dir?.data?.removeLast()

            val nameOfLastFragmentInDirectory = dir?.data?.last()

            if (nameOfLastFragmentInDirectory != null) {
                val fragment = fragmentManager.findFragmentByTag(nameOfLastFragmentInDirectory)
                if (fragment != null)
                    fragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commit()
            }
        } else {
            // if there is one or less(this must be impossible)

            val prevDir = rootTag.indexOfFirst { it.dirName == curDir } - 1
            if (prevDir != -1) {
                curDir = rootTag[prevDir].dirName
                back()
            } else
                closeApp()
        }
    }

    private fun closeApp() {
        finishApp()
    }
}