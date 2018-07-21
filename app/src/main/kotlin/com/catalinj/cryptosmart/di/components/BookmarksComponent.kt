package com.catalinj.cryptosmart.di.components

import com.catalinj.cryptosmart.di.annotations.scopes.BookmarksScope
import com.catalinj.cryptosmart.di.modules.bookmarks.BookmarksModule
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.view.BookmarksFragment
import com.catalinj.cryptosmart.presentationlayer.features.coindisplayoptions.view.CoinDisplayOptionsToolbar
import dagger.Subcomponent

/**
 * Created by catalin on 14/05/2018.
 */
@BookmarksScope
@Subcomponent(modules = [BookmarksModule::class])
abstract class BookmarksComponent {

    abstract fun inject(bookmarksFragment: BookmarksFragment)

    abstract fun inject(displayOptionsToolbar: CoinDisplayOptionsToolbar)
}