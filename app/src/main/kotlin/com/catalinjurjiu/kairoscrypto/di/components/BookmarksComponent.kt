package com.catalinjurjiu.kairoscrypto.di.components

import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.BookmarksScope
import com.catalinjurjiu.kairoscrypto.di.modules.bookmarks.BookmarksModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.bookmarks.view.BookmarksFragment
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.view.CoinDisplayOptionsToolbar
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.widgets.scrolltotop.view.ScrollToTopFloatingActionButton
import dagger.Subcomponent

/**
 * Created by catalin on 14/05/2018.
 */
@BookmarksScope
@Subcomponent(modules = [BookmarksModule::class])
abstract class BookmarksComponent {

    abstract fun inject(bookmarksFragment: BookmarksFragment)

    abstract fun inject(displayOptionsToolbar: CoinDisplayOptionsToolbar)

    abstract fun inject(scrollToTopWidget: ScrollToTopFloatingActionButton)
}