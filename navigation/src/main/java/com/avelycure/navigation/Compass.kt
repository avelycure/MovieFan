package com.avelycure.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.avelycure.core_navigation.*
import java.io.Serializable
import kotlin.concurrent.thread

/**
 * Class to navigate between fragments
 */
class Compass() : Navigator, Serializable {

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
     * Removing fragment from backstack
     */
    override fun openLastFragmentInDirectory(dir: String) {
        if (directoryIsNotEmpty(dir)) {
            val screen = fragments.find { it.dirName == dir }?.fragments?.last()

            if (screen != null) {

                //if merged in one transaction the fragment is not added to top
                fragmentManager
                    .beginTransaction()
                    .remove(screen.fragment)
                    .commit()


                //    if (dir == screen.directory) {
                fragmentManager
                    .beginTransaction()
                    .add(containerId, screen.fragment)
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
        //current directory
        val dir = fragments.find { it.dirName == curDir }

        //if there is more than one fragment in this directory
        if (dir?.fragments?.size ?: 0 > 1) {

            val screen = dir?.fragments?.removeLast()

            if (screen != null) {
                fragmentManager
                    .beginTransaction()
                    .remove(screen.fragment)
                    .commit()
            }

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

    // Recreating

    private fun saveState(outState: Bundle) {
        var counter = 0
        for (i in 0 until fragments.size) {

            val count = fragments[i].fragments.size

            for (j in 0 until count) {
                fragmentManager.putFragment(
                    outState,
                    (counter).toString(),
                    fragments[i].fragments[j].fragment
                )
                counter++
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
        var counter = 0

        for (i in 0 until fragments.size) {

            for (j in 0 until fragments[i].fragments.size) {
                val f = fragmentManager.getFragment(
                    savedInstanceState,
                    (counter).toString()
                )
                if (f != null)
                    fragments[i].fragments[j].fragment = f
                counter++
            }
        }
    }

    override fun onDestroy(outState: Bundle) {
        saveState(outState)
    }


    //Debugging

    private fun printFM(name: String, frags: List<Fragment>) {
        val text = buildString {
            for (f in frags)
                append(f.javaClass.simpleName, " ")
        }
        Log.d("mytag", "$name : $text")
    }

    private fun printTags(name: String, frags: List<DirectoryStack>) {
        Log.d("mytag", "***************")
        for (d in frags) {
            val text = buildString {
                for (f in d.fragments)
                    append("(${f.tag}, ${f.fragment.javaClass.simpleName}) ")
            }
            Log.d("mytag", "${d.dirName} : $text")
        }
        Log.d("mytag", "***************")
    }
}