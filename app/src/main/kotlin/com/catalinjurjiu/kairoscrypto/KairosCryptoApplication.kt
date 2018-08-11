package com.catalinjurjiu.kairoscrypto

import android.app.Application
import com.catalinjurjiu.kairoscrypto.datalayer.userprefs.KairosCryptoUserSettings
import com.catalinjurjiu.kairoscrypto.di.components.AppComponent
import com.catalinjurjiu.kairoscrypto.di.components.DaggerAppComponent
import com.catalinjurjiu.kairoscrypto.di.modules.app.AppModule
import com.catalinjurjiu.kairoscrypto.di.modules.data.NetworkModule
import com.catalinjurjiu.kairoscrypto.di.modules.data.PersistenceModule
import com.catalinjurjiu.kairoscrypto.di.modules.data.RepositoryModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.formatter.CurrencyFormatter
import com.catalinjurjiu.common.Holder
import com.squareup.leakcanary.LeakCanary
import javax.inject.Inject

/**
 * Represents the Kairos Crypto application context. Used for App-Global config & acts as a injection
 * root for Dagger2 components.
 *
 * Main Activity can be found in the "presentationlayer" package.
 *
 * Created by catalinj on 04.02.2018.
 */
@Suppress("unused")
class KairosCryptoApplication : Application(), Holder<AppComponent> {

    override lateinit var component: AppComponent

    override val hasComp: Boolean
        get() = ::component.isInitialized

    @Inject
    protected lateinit var userSettings: KairosCryptoUserSettings

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
        component.inject(this)
        CurrencyFormatter.refreshLocale(context = this)
        println("Application onCreate")
    }

}