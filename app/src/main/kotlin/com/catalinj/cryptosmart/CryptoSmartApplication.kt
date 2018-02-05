package com.catalinj.cryptosmart

import android.app.Application
import com.catalinj.cryptosmart.di.components.AppComponent
import com.catalinj.cryptosmart.di.components.CoinListComponent
import com.catalinj.cryptosmart.di.components.DaggerAppComponent
import com.catalinj.cryptosmart.di.modules.coinlist.CoinListModule
import com.catalinj.cryptosmart.di.modules.general.AppModule
import com.catalinj.cryptosmart.di.modules.general.NetworkModule
import com.catalinj.cryptosmart.di.modules.general.PersistanceModule

/**
 * Created by catalinj on 04.02.2018.
 */
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
        println("Application onCreate")

    }

    override fun getCoinListComponent(): CoinListComponent {
        return appComponent2.getCoinListComponent(CoinListModule())
    }

    override fun getAppComponent(): AppComponent {
        return appComponent2
    }
}