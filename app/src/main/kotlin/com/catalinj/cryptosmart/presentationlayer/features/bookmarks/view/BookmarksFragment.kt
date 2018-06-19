package com.catalinj.cryptosmart.presentationlayer.features.bookmarks.view

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.components.BookmarksComponent
import com.catalinj.cryptosmart.di.modules.bookmarks.BookmarksModule
import com.catalinj.cryptosmart.presentationlayer.MainActivity
import com.catalinj.cryptosmart.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.contract.BookmarksContract
import com.catalinj.cryptosmart.presentationlayer.features.bookmarks.model.BookmarksCoin
import com.catalinjurjiu.wheelbarrow.InjectorFragment
import com.example.cryptodrawablesprovider.ImageHelper
import kotlinx.android.synthetic.main.layout_fragment_bookmarks.view.*
import javax.inject.Inject

/**
 * Created by catalin on 14/05/2018.
 */
class BookmarksFragment : InjectorFragment<BookmarksComponent>(),
        BookmarksContract.BookmarksView,
        BackEventAwareComponent {

    override val name: String = TAG

    @Inject
    protected lateinit var bookmarksPresenter: BookmarksContract.BookmarksPresenter
    @Inject
    protected lateinit var imageHelper: ImageHelper<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: BookmarksListAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    //android fragment lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_fragment_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showBottomNavigation()
        bookmarksPresenter.navigator = (activity as MainActivity).navigator
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
        swipeRefreshLayout = view.swiperefreshlayout_bookmarks_list
        swipeRefreshLayout.setOnRefreshListener {
            bookmarksPresenter.userPullToRefresh()
        }
        recyclerView = view.recyclerview_bookmarks_list
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerViewAdapter = BookmarksListAdapter(context = context!!,
                primaryCurrency = CurrencyRepresentation.USD,
                coins = mutableListOf(),
                imageHelper = imageHelper) {
            bookmarksPresenter.coinSelected(it)
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
    override fun setListData(primaryCurrency: CurrencyRepresentation,
                             bookmarksList: List<BookmarksCoin>) {
        recyclerViewAdapter.primaryCurrency = primaryCurrency
        recyclerViewAdapter.coins = bookmarksList.toMutableList()
        recyclerViewAdapter.notifyDataSetChanged()
    }

    override fun showLoadingIndicator() {
        //do nothing, each card displays its own loading indicator
    }

    override fun hideLoadingIndicator() {
        swipeRefreshLayout.isRefreshing = false
    }

    //end bookmarks view methods
    class Factory(val activityComponent: ActivityComponent) : InjectorFragmentFactory<BookmarksComponent>() {

        override fun onCreateFragment(): InjectorFragment<BookmarksComponent> {
            return BookmarksFragment()
        }

        override fun onCreateInjector(): BookmarksComponent {
            return activityComponent.getBookmarksComponent(BookmarksModule())
        }
    }

    companion object {
        const val TAG = "BookmarksView"
    }
}