package com.catalinj.cryptosmart.di.components

import com.catalinj.cryptosmart.di.annotations.scopes.ActivityScope
import com.catalinj.cryptosmart.di.modules.bookmarks.BookmarksModule
import com.catalinj.cryptosmart.di.modules.coindetails.CoinDetailsModule
import com.catalinj.cryptosmart.di.modules.coinlist.CoinListModule
import com.catalinj.cryptosmart.presentationlayer.MainActivity
import dagger.Subcomponent

/**
 * Created by catalinj on 04.02.2018.
 */
@ActivityScope
@Subcomponent
abstract class ActivityComponent {

    abstract fun inject(mainActivity: MainActivity)

    abstract fun getCoinListComponent(coinListModule: CoinListModule): CoinListComponent

    abstract fun getCoinDetailsComponent(coinListModule: CoinDetailsModule): CoinDetailsComponent

    abstract fun getBookmarksComponent(bookmarksModule: BookmarksModule): BookmarksComponent
}