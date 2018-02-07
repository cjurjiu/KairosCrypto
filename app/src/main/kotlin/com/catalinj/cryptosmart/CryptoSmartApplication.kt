package com.catalinj.cryptosmart

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.util.Log
import com.catalinj.cryptosmart.di.components.AppComponent
import com.catalinj.cryptosmart.di.components.DaggerAppComponent
import com.catalinj.cryptosmart.di.modules.general.AppModule
import com.catalinj.cryptosmart.di.modules.general.NetworkModule
import com.catalinj.cryptosmart.di.modules.general.PersistanceModule


/**
 * Created by catalinj on 04.02.2018.
 */
@Suppress("unused")
class CryptoSmartApplication : Application, DependencyRoot {

    private val appComponent2: AppComponent by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        DaggerAppComponent.builder()
                .appModule(AppModule(applicationContext))
                .networkModule(NetworkModule())
                .persistanceModule(PersistanceModule())
                .build()
    }

    constructor() : super() {
        println("Application constructor")
    }

    override fun onCreate() {
        super.onCreate()
        getProcessName(this)
        println("Application onCreate")
    }

    override fun getAppComponent(): AppComponent {
        return appComponent2
    }

    fun getProcessName(app: CryptoSmartApplication): String {
        var currentProcName = ""
        val pid = android.os.Process.myPid()
        val manager: ActivityManager = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                currentProcName = processInfo.processName
                break
            }
        }
        Log.d("Cata", "Process: $currentProcName")
        return currentProcName
    }
}