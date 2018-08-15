package com.catalinjurjiu.kairoscrypto.presentationlayer.features.bookmarks.contract

import com.catalinjurjiu.kairoscrypto.businesslayer.model.BookmarksCoin
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.navigation.Navigator
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.presenter.MvpPresenter
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.LoadingView
import com.catalinjurjiu.kairoscrypto.presentationlayer.common.view.MvpView
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.coindisplayoptions.contract.CoinsDisplayOptionsContract

/**
 * Contract which defines the view-presenter interactions that occur in the Bookmarks screen.
 *
 * Created by catalin on 14/05/2018.
 */
interface BookmarksContract {

    /**
     * Interface definition for a presenter in charge of the Bookmarks screen.
     *
     * This presenter is also a [CoinDisplayController][CoinsDisplayOptionsContract.CoinDisplayController],
     * meaning it is aware of the fact that the list of bookmarks can be displayed in various currencies,
     * or using different snapshots.
     */
    interface BookmarksPresenter : MvpPresenter<BookmarksPresenter, BookmarksView>,
            CoinsDisplayOptionsContract.CoinDisplayController {

        /**
         * The [Navigator] instance used by this presenter when navigation to a different screen is
         * required.
         *
         * If it relies on platform-specific components with limited lifecycle (i.e. Activity context),
         * then make sure to deallocate and/or update the navigator when needed, to prevent leaks.
         */
        var navigator: Navigator?

        /**
         * Notifies the presenter that the user has selected a bookmark.
         *
         * @param bookmarksCoin the coin selected by the user.
         */
        fun coinSelected(bookmarksCoin: BookmarksCoin)

        /**
         * Notifies that the user requested a data refresh via a "pull-to-refresh" gesture.
         */
        fun userPullToRefresh()
    }

    /**
     * Interface definition for the component in charge of displaying the data specific for the
     * Bookmarks screen.
     *
     * This view is also a [LoadingView].
     */
    interface BookmarksView : MvpView<BookmarksPresenter, BookmarksView>, LoadingView {

        /**
         * Sets the data that this view needs to display.
         *
         * @param bookmarksList the list of [BookmarksCoin] items that need to be displayed
         */
        fun setData(bookmarksList: List<BookmarksCoin>)

        /**
         * Re-queries the [BookmarksPresenter] for data, then redraws the UI.
         */
        fun refreshContent()

        /**
         * Shows an error message to the user.
         *
         * @param errorCode the error code associated with the error that has occurred
         * @param retryAction the handler to be invoked when the user attempts to retry the failed
         * action.
         */
        fun showError(errorCode: ErrorCode, retryAction: () -> Unit)

        /**
         * Hides or reveals the view's content (all content, or only partial - view specific).
         *
         * @param isVisible `true` if the content should be visible, `false` otherwise
         */
        fun setContentVisible(isVisible: Boolean)
    }
}