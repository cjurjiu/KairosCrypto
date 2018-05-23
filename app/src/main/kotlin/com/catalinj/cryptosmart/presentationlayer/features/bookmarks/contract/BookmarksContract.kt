package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract

import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.presentationlayer.common.navigation.Navigator
import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter
import com.catalinj.cryptosmart.presentationlayer.common.view.MvpView

/**
 * Created by catalin on 14/05/2018.
 */
interface BookmarksContract {
    interface BookmarksPresenter : MvpPresenter<BookmarksPresenter, BookmarksView> {
        var navigator: Navigator?
        fun coinSelected(cryptoCoin: CryptoCoin)
    }

    interface BookmarksView : MvpView<BookmarksPresenter, BookmarksView> {
        fun setListData(primaryCurrency: CurrencyRepresentation, bookmarksList: List<CryptoCoin>)
    }
}