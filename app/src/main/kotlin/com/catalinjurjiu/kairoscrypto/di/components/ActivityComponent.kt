package com.catalinjurjiu.kairoscrypto.di.components

import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.ActivityScope
import com.catalinjurjiu.kairoscrypto.di.modules.activity.ActivityModule
import com.catalinjurjiu.kairoscrypto.di.modules.bookmarks.BookmarksModule
import com.catalinjurjiu.kairoscrypto.di.modules.coindetails.CoinDetailsModule
import com.catalinjurjiu.kairoscrypto.di.modules.coinlist.CoinListModule
import com.catalinjurjiu.kairoscrypto.di.modules.settings.SettingsModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.MainActivity
import dagger.Subcomponent

/**
 * Created by catalinj on 04.02.2018.
 */
@ActivityScope
@Subcomponent(modules = [(ActivityModule::class)])
abstract class ActivityComponent {

    abstract fun inject(mainActivity: MainActivity)

    abstract fun getCoinListComponent(coinListModule: CoinListModule): CoinListComponent

    abstract fun getCoinDetailsComponent(coinListModule: CoinDetailsModule): CoinDetailsComponent

    abstract fun getBookmarksComponent(bookmarksModule: BookmarksModule): BookmarksComponent

    abstract fun getSettingsComponent(settingsModule: SettingsModule): SettingsComponent
}