package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.components.BookmarksComponent
import com.catalinj.cryptosmart.di.modules.bookmarks.BookmarksModule
import com.catalinj.cryptosmart.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract.BookmarksContract
import com.catalinjurjiu.smartpersist.DaggerFragment
import kotlinx.android.synthetic.main.layout_fragment_bookmarks.view.*
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: BookmarksListAdapter

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
    //END android fragment lifecycle

    //mvp view methods
    override fun initialise() {
        Log.d("Cata", "$TAG#initialise")
        val view = view!!
        recyclerView = view.recyclerview_bookmarks_list
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerViewAdapter = BookmarksListAdapter(context!!, CurrencyRepresentation.USD, emptyList()) {
            Log.d("Cata", "Bookmarked clicked -> ${it.name}")
        }
        recyclerView.adapter = recyclerViewAdapter
    }

    override fun getPresenter(): BookmarksContract.BookmarksPresenter {
        return bookmarksPresenter
    }

    override fun onBack(): Boolean {
        return false
    }
    //end mvp view methods

    //bookmarks view methods
    override fun setListData(primaryCurrency: CurrencyRepresentation, bookmarksList: List<CryptoCoin>) {
        recyclerViewAdapter.primaryCurrency = primaryCurrency
        recyclerViewAdapter.coins = bookmarksList
        recyclerViewAdapter.notifyDataSetChanged()
    }
    //end bookmarks view methods

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