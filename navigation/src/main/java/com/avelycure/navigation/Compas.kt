package com.avelycure.navigation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Class to navigate between fragments
 */
class Compas(
    private val context: Context,
    val containerId: Int,
    directoriesNames: List<String>
) : Navigator {
    private val fragmentManager: FragmentManager =
        (context as AppCompatActivity).supportFragmentManager

    private val directories: LinkedHashMap<String, MutableList<String>> = LinkedHashMap()

    private val fragmentContainerMap: HashMap<String, Int> = hashMapOf()

    init {
        for (name in directoriesNames) {
            directories[name] = mutableListOf()
        }
    }

    override fun add(directory: String, tag: String, fragment: Fragment) {
        directories[directory]?.add(tag)
        fragmentManager
            .beginTransaction()
            .add(containerId,fragment)
            .addToBackStack(tag)
            .commit()
    }

    /**
     * If it has 1 or more fragments in stack
     */
    private fun directoryIsEmpty(dir: String): Boolean {
        return (directories[dir]?.size ?: 0) >= 1
    }

    //todo think about this
    private fun getLastFragmentInDirectory(currentDirectory: String): Fragment {
        if (!directoryIsEmpty(currentDirectory))
            return fragmentManager.findFragmentByTag(directories[currentDirectory]?.last())
                ?: Fragment()
        return Fragment()
    }

    private fun navigateToLastFragment(currentDirectory: String){
        fragmentManager
            .beginTransaction()
            .replace(containerId, getLastFragmentInDirectory(currentDirectory))
            .commit()
    }

    override fun back(currentDirectory: String) {
        if (!directoryIsEmpty(currentDirectory)) {
            directories[currentDirectory]?.removeLast()
            navigateToLastFragment(currentDirectory)
        } else {
            val index = directories.keys.indexOf(currentDirectory)
            if (index >= 1)
                back(directories.keys.toList()[index - 1])
            else
                closeApp()
        }
    }

    private fun closeApp() {

    }
}