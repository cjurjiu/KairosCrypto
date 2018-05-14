package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.components.BookmarksComponent
import com.catalinj.cryptosmart.di.modules.bookmarks.BookmarksModule
import com.catalinj.cryptosmart.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract.BookmarksContract
import com.catalinjurjiu.smartpersist.DaggerFragment
import javax.inject.Inject

/**
 * Created by catalin on 14/05/2018.
 */
class BookmarksFragment : DaggerFragment<BookmarksComponent>(),
        BookmarksContract.BookmarksView,
        BackEventAwareComponent {

    override val name: String = TAG

    @Inject
    protected lateinit var bookmarksPresenter: BookmarksContract.BookmarksPresenter

    //android fragment lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_fragment_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookmarksPresenter.viewAvailable(this)
    }

    override fun onStart() {
        super.onStart()
        bookmarksPresenter.startPresenting()
    }

    override fun onStop() {
        super.onStop()
        bookmarksPresenter.stopPresenting()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bookmarksPresenter.viewDestroyed()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    //END android fragment lifecycle

    //mvp view methods
    override fun initialise() {
        Log.d("Cata", "$TAG#initialise")
    }

    override fun getPresenter(): BookmarksContract.BookmarksPresenter {
        return bookmarksPresenter
    }

    override fun onBack(): Boolean {
        return false
    }
    //end mvp view methods

    class Factory(val activityComponent: ActivityComponent) : DaggerFragmentFactory<BookmarksComponent>() {

        override fun onCreateFragment(): DaggerFragment<BookmarksComponent> {
            return BookmarksFragment()
        }

        override fun onCreateDaggerComponent(): BookmarksComponent {
            return activityComponent.getBookmarksComponent(BookmarksModule())
        }
    }

    companion object {
        const val TAG = "BookmarksView"
    }
}