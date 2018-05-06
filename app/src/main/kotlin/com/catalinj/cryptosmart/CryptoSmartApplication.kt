package com.catalinj.cryptosmart

import android.app.Application
import com.catalinj.cryptosmart.di.components.AppComponent
import com.catalinj.cryptosmart.di.components.DaggerAppComponent
import com.catalinj.cryptosmart.di.modules.app.AppModule
import com.catalinj.cryptosmart.di.modules.data.NetworkModule
import com.catalinj.cryptosmart.di.modules.data.PersistenceModule
import com.catalinj.cryptosmart.di.modules.data.RepositoryModule
import com.catalinjurjiu.common.Holder
import com.squareup.leakcanary.LeakCanary

/**
 * Represents the CryptoSmart application context. Used for App-Global config & acts as a injection
 * root for Dagger2 components.
 *
 * Main Activity can be found in the "presentationlayer" package.
 *
 * Created by catalinj on 04.02.2018.
 */
@Suppress("unused")
class CryptoSmartApplication : Application(), Holder<AppComponent> {

    override lateinit var component: AppComponent

    override val hasComp: Boolean
        get() = ::component.isInitialized

    private val cryptoAppComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        DaggerAppComponent.builder()
                .appModule(AppModule(applicationContext))
                .networkModule(NetworkModule())
                .repositoryModule(RepositoryModule())
                .persistenceModule(PersistenceModule())
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
        component = cryptoAppComponent
        println("Application onCreate")
    }

}