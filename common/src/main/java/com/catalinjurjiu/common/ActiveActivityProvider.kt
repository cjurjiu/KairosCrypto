package com.catalinjurjiu.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

class ActiveActivityProvider(application: Application) : Application.ActivityLifecycleCallbacks {

    init {
        application.registerActivityLifecycleCallbacks(this)
    }

    lateinit var activeActivity: Activity

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        Log.d(TAG, "New active Activity instance: ${activity.toString()}.")
        activeActivity = activity!!
    }

    override fun onActivityStarted(activity: Activity?) {
        //ignored
    }

    override fun onActivityResumed(activity: Activity?) {
        //ignored
    }

    override fun onActivityPaused(activity: Activity?) {
        //ignored
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        //ignored
    }

    override fun onActivityStopped(activity: Activity?) {
        //ignored
    }

    override fun onActivityDestroyed(activity: Activity?) {
        Log.d(TAG, "Activity instance destroyed: ${activity.toString()}.")
    }

    private companion object {
        const val TAG = "ActiveActivityProvider"
    }
}