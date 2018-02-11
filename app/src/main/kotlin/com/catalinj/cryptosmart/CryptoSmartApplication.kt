package com.catalinj.cryptosmart

import android.app.Application
import com.catalinj.cryptosmart.di.DependencyRoot
import com.catalinj.cryptosmart.di.components.AppComponent
import com.catalinj.cryptosmart.di.components.DaggerAppComponent
import com.catalinj.cryptosmart.di.modules.general.AppModule
import com.catalinj.cryptosmart.di.modules.general.NetworkModule
import com.catalinj.cryptosmart.di.modules.general.PersistanceModule
import com.squareup.leakcanary.LeakCanary


/**
 * Created by catalinj on 04.02.2018.
 */
@Suppress("unused")
class CryptoSmartApplication : Application(), DependencyRoot {

    private val cryptoAppComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        DaggerAppComponent.builder()
                .appModule(AppModule(applicationContext))
                .networkModule(NetworkModule())
                .persistanceModule(PersistanceModule())
                .build()
    }

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this);

        println("Application onCreate")
    }

    override fun getAppComponent(): AppComponent {
        return cryptoAppComponent
    }
}