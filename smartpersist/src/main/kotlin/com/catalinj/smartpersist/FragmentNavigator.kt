package com.catalinj.smartpersist

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.catalinj.smartpersist.markes.HasRetainable

/**
 * Created by catalinj on 10.02.2018.
 */
class FragmentNavigator(private var fragmentManager: FragmentManager, previousRetainable: Map<String, Any> = emptyMap()) : HasRetainable<Map<String, Any>> {

    override val retainable: Map<String, Any>
        get() {
            return mapOf(Pair(TAG, inBackStackList))
        }

    private val inBackStackList: MutableSet<String> = mutableSetOf()
    private var backStackEntryCount: Int

    init {
        @Suppress("UNCHECKED_CAST")
        inBackStackList.addAll((previousRetainable[TAG] as Set<String>?) ?: emptySet())
        backStackEntryCount = inBackStackList.size
    }

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
        fragmentManager.addOnBackStackChangedListener(backStackChangedListener)
    }

    fun add(@IdRes container: Int, frag: Fragment, tag: String) {
        fragmentManager.beginTransaction()
                .add(container, frag, tag)
                .commit()
    }

    fun addWithBackStack(@IdRes container: Int, frag: Fragment, tag: String) {
        val existingFragTag: String = fragmentManager.findFragmentById(container).tag ?: ""
        fragmentManager.beginTransaction()
                .add(container, frag, tag)
                .addToBackStack(BackStackEntryName(existingFragTag, tag).toString())
                .commit()
    }

    fun replace(@IdRes container: Int, frag: Fragment, tag: String) {
        fragmentManager.beginTransaction()
                .replace(container, frag, tag)
                .commitNow()
    }

    fun replaceWithBackStack(@IdRes container: Int, frag: Fragment, tag: String) {

        val existingFragTag: String = fragmentManager.findFragmentById(container)?.tag ?: ""

        fragmentManager.beginTransaction()
                .replace(container, frag, tag)
                .addToBackStack(BackStackEntryName(existingFragTag, tag).toString())
                .commit()
    }

    fun listFragments(): List<Fragment> {
        val fragments = mutableSetOf<Fragment>()
        fragments.addAll(fragmentManager.fragments)
        inBackStackList.forEach {
            val foundFragment = fragmentManager.findFragmentByTag(it)
            foundFragment?.let {
                fragments.add(it)
            }
        }
        return fragments.toList()
    }

    fun popBackStackImmediate(): Boolean {
        return fragmentManager.popBackStackImmediate()
    }

    private data class BackStackEntryName(val oldFragment: String, val newFragment: String) {
        override fun toString(): String {
            return "$oldFragment$SEPARATOR$newFragment"
        }

        companion object {
            const val SEPARATOR = "$"
        }
    }

    private companion object {
        private const val TAG = "FragmentNavigator"
    }
}