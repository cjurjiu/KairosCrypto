package com.catalinj.cryptosmart.di.modules.bookmarks

import com.catalinj.cryptosmart.datalayer.database.CryptoSmartDb
import com.catalinj.cryptosmart.datalayer.userprefs.CryptoSmartUserSettings
import com.catalinj.cryptosmart.di.annotations.scopes.BookmarksScope
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract.BookmarksContract
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.presenter.BookmarksPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by catalin on 14/05/2018.
 */
@Module
class BookmarksModule {

    @Provides
    @BookmarksScope
    fun provideBookmarksPresenter(db: CryptoSmartDb,
                                  userSettings: CryptoSmartUserSettings)
            : BookmarksContract.BookmarksPresenter {
        return BookmarksPresenter(cryptoSmartDb = db, userSettings = userSettings)
    }
}