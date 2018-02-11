package com.catalinj.smartpersist

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Created by catalinj on 10.02.2018.
 */
class FragmentNavigator(private var fragmentManager: FragmentManager, previousBackStackList: Set<String> = emptySet()) {

    private val inBackStackList: MutableSet<String> = mutableSetOf()
    private var backStackEntryCount: Int = -1

//  private var backStackChangedListener = object : FragmentManager.OnBackStackChangedListener {
//
//        override fun onBackStackChanged() {
//            val increased = backStackEntryCount < fragmentManager.backStackEntryCount
//            if (increased) {
//                for (i in 0 until fragmentManager.backStackEntryCount) {
//                    val backStackEntry: FragmentManager.BackStackEntry = fragmentManager.getBackStackEntryAt(i)
//                    inBackStackList.add(backStackEntry.name.substringBefore(BackStackEntryName.SEPARATOR))
//                }
//            } else {
//                val savedList: MutableSet<String> = mutableSetOf()
//                for (i in 0 until fragmentManager.backStackEntryCount) {
//                    val backStackEntry: FragmentManager.BackStackEntry = fragmentManager.getBackStackEntryAt(i)
//                    savedList.remove(backStackEntry.name.substringBefore(BackStackEntryName.SEPARATOR))
//                }
//                if (savedList.isEmpty()) {
//                    inBackStackList.clear()
//                } else {
//                    inBackStackList.remove(savedList.first())
//                }
//            }
//            backStackEntryCount = inBackStackList.size
//        }
//    }

    private var backStackChangedListener: () -> Unit = {
        val increased = backStackEntryCount < fragmentManager.backStackEntryCount
        if (increased) {
            for (i in 0 until fragmentManager.backStackEntryCount) {
                val backStackEntry: FragmentManager.BackStackEntry = fragmentManager.getBackStackEntryAt(i)
                inBackStackList.add(backStackEntry.name.substringBefore(BackStackEntryName.SEPARATOR))
            }
        } else {
            val savedList: MutableSet<String> = mutableSetOf()
            for (i in 0 until fragmentManager.backStackEntryCount) {
                val backStackEntry: FragmentManager.BackStackEntry = fragmentManager.getBackStackEntryAt(i)
                savedList.remove(backStackEntry.name.substringBefore(BackStackEntryName.SEPARATOR))
            }
            if (savedList.isEmpty()) {
                inBackStackList.clear()
            } else {
                inBackStackList.remove(savedList.first())
            }
        }
        backStackEntryCount = inBackStackList.size
    }

    init {
        inBackStackList.addAll(previousBackStackList)
        fragmentManager.addOnBackStackChangedListener(backStackChangedListener)
    }

    fun add(@IdRes container: Int, frag: Fragment, tag: String) {
        fragmentManager.beginTransaction()
                .add(container, frag, tag)
                .commitNow()
    }

    fun replace(@IdRes container: Int, frag: Fragment, tag: String) {
        fragmentManager.beginTransaction()
                .replace(container, frag, tag)
                .commitNow()
    }

    fun replaceWithBackStack(@IdRes container: Int, frag: Fragment, tag: String) {

        val existingFragTag: String = fragmentManager.findFragmentById(container).tag ?: ""

        fragmentManager.beginTransaction()
                .replace(container, frag, tag)
                .addToBackStack(BackStackEntryName(existingFragTag, tag).toString())
                .commit()
    }

    fun listFragments(): List<Fragment> {
        val fragments = mutableSetOf<Fragment>()
        fragments.addAll(fragmentManager.fragments)
        inBackStackList.forEach { fragments.add(fragmentManager.findFragmentByTag(it)) }
        return fragments.toList()
    }

    fun popBackStackImmediate(): Boolean {
        return fragmentManager.popBackStackImmediate()
    }

//    companion object {
//        lateinit var instance: FragmentNavigator
//
//        fun doInit(fragManager: FragmentManager) {
//            instance = FragmentNavigator(fragManager)
//            instance.fragmentManager.addOnBackStackChangedListener(instance.backStackChangedListener)
//        }
//
//        fun updateFragManager(fragManager: FragmentManager) {
//            instance.fragmentManager.removeOnBackStackChangedListener(instance.backStackChangedListener)
//            instance.fragmentManager = fragManager
//            instance.fragmentManager.addOnBackStackChangedListener(instance.backStackChangedListener)
//        }
//    }

    private data class BackStackEntryName(val oldFragment: String, val newFragment: String) {
        override fun toString(): String {
            return "$oldFragment$$newFragment"
        }

        companion object {
            const val SEPARATOR = "$"
        }
    }
}