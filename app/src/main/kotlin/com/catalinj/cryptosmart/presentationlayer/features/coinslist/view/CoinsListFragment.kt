package com.catalinj.cryptosmart.presentationlayer.features.coinslist.view

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.components.CoinListComponent
import com.catalinj.cryptosmart.di.modules.coinlist.CoinListModule
import com.catalinj.cryptosmart.presentationlayer.MainActivity
import com.catalinj.cryptosmart.presentationlayer.common.functional.BackEventAwareComponent
import com.catalinj.cryptosmart.presentationlayer.features.coinslist.contract.CoinsListContract
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.SelectionItem
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.toParcelableSelectionItem
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.model.toSelectionItem
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.view.ListenerType
import com.catalinj.cryptosmart.presentationlayer.features.selectiondialog.view.SelectionListDialog
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.wheelbarrow.InjectorFragment
import com.example.cryptodrawablesprovider.ImageHelper
import kotlinx.android.synthetic.main.layout_fragment_coin_list.view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by catalinj on 21.01.2018.
 */
class CoinsListFragment :
        InjectorFragment<CoinListComponent>(),
        NamedComponent,
        BackEventAwareComponent,
        CoinsListContract.CoinsListView {

    override val name: String = TAG

    @Inject
    protected lateinit var coinListPresenter: CoinsListContract.CoinsListPresenter

    @Inject
    protected lateinit var imageHelper: ImageHelper<String>

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var recyclerView: RecyclerView

    private lateinit var recyclerViewAdapter: CoinListAdapter

    private lateinit var recyclerViewLayoutManager: LinearLayoutManager

    private val onChangeCurrencyButtonClickedListener = View.OnClickListener { coinListPresenter.changeCurrencyButtonPressed() }

    private val onSnapshotButtonClickedListener = View.OnClickListener { coinListPresenter.selectSnapshotButtonPressed() }

    class Factory(private val activityComponent: ActivityComponent) : InjectorFragmentFactory<CoinListComponent>() {

        override fun onCreateFragment(): InjectorFragment<CoinListComponent> {
            val f = CoinsListFragment()
            //do some other initializations, set arguments
            return f
        }

        override fun onCreateInjector(): CoinListComponent {
            return activityComponent.getCoinListComponent(CoinListModule())
        }
    }

    //view methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        Log.d(TAG, "CoinsListFragment${hashCode().toString(16)}#onCreate.injector:" + injector.hashCode().toString(16) + " presenter:" + coinListPresenter.hashCode().toString(16))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "CoinsListFragment#onCreateView")
        return inflater.inflate(R.layout.layout_fragment_coin_list, container, false)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinListPresenter.navigator = (activity as MainActivity).navigator
        (activity as MainActivity).showBottomNavigation()
        Log.d(TAG, "CoinsListFragment#onViewCreated")
        coinListPresenter.viewAvailable(this)
    }

    override fun initialise() {
        val view = view!!
        val appCompatActivity = (activity as AppCompatActivity)
        initToolbar(rootView = view, appCompatActivity = appCompatActivity)
        initRecyclerView(rootView = view, appCompatActivity = appCompatActivity)
        initSwipeRefreshLayout(rootView = view)
        rebindDialogIfActive()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "CoinsListFragment#onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "CoinsListFragment#onPause")
    }

    override fun onStart() {
        super.onStart()
        coinListPresenter.startPresenting()
        Log.d(TAG, "CoinsListFragment#onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "CoinsListFragment#onStop")
        coinListPresenter.stopPresenting()
        Log.d(TAG, "CoinsListFragment#onStop. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "CoinsListFragment#onSaveInstanceState. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "CoinsListFragment#onDestroyView")
        recyclerView.clearOnScrollListeners()
        coinListPresenter.viewDestroyed()
        Log.d(TAG, "CoinsListFragment#onDestroyView. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "CoinsListFragment#onDestroy")
        Log.d(TAG, "CoinsListFragment#onDestroy. isRemoving:$isRemoving isActivityFinishing:${activity?.isFinishing} " +
                "a2:${activity?.isChangingConfigurations}")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "CoinsListFragment#onDetach")
    }

    override fun onBack(): Boolean {
        Log.d(TAG, "CoinsListFragment#onBack")
        return false
    }

    override fun getPresenter(): CoinsListContract.CoinsListPresenter {
        return coinListPresenter
    }

    //coin list view presenter
    override fun setListData(data: List<CryptoCoin>) {
        recyclerViewAdapter.coins = data
        recyclerViewAdapter.currency = coinListPresenter.getSelectedCurrency()
        recyclerViewAdapter.notifyDataSetChanged()
    }

    override fun showLoadingIndicator() {
        Log.d("Cata", "Loading started")
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        Log.d("Cata", "Loading stopped")
        swipeRefreshLayout.isRefreshing = false
    }

    override fun scrollTo(scrollPosition: Int) {
        recyclerView.scrollToPosition(scrollPosition)
    }

    override fun openChangeCurrencyDialog(selectionItems: List<SelectionItem>) {
        showSelectionDialog(dialogType = CoinListSelectionDialogType.ChangeCurrency, data = selectionItems)
    }

    override fun openSelectSnapshotDialog(selectionItems: List<SelectionItem>) {
        showSelectionDialog(dialogType = CoinListSelectionDialogType.SelectSnapshot, data = selectionItems)
    }

    override fun setContentVisible(isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    private fun rebindDialogIfActive() {
        val activeFragmentList = arrayOf(CoinListSelectionDialogType.ChangeCurrency,
                CoinListSelectionDialogType.SelectSnapshot)
                .mapNotNull {
                    val fragment = fragmentManager?.findFragmentByTag(it.typeName)
                    return@mapNotNull if (fragment != null) {
                        Pair(it, fragment)
                    } else {
                        null
                    }
                }
                .apply {
                    if (size > 1) {
                        val activeFragments: String? = map { it.first.typeName }.reduce { acc, s -> "$acc, $s" }
                        throw IllegalStateException("More than 1 selection dialogs present on screen." +
                                "Active dialogs: $activeFragments")
                    }
                }
        if (activeFragmentList.isNotEmpty()) {
            activeFragmentList
                    .first()
                    .let {
                        val selectionDialog = it.second as SelectionListDialog
                        val listener = getListenerForDialogType(it.first)
                        selectionDialog.setListener(listener)
                    }
        }
    }

    private fun initToolbar(rootView: View, appCompatActivity: AppCompatActivity) {
        val toolbar: Toolbar = rootView.findViewById(R.id.my_toolbar)
        appCompatActivity.setSupportActionBar(toolbar)

        //set the change currency button clicked listener
        val changeCurrencyButton = toolbar.findViewById<ImageButton>(R.id.button_change_currency)
        changeCurrencyButton?.setOnClickListener(onChangeCurrencyButtonClickedListener)
        //set the change currency button clicked listener
        val selectSnapshotButton = toolbar.findViewById<ImageButton>(R.id.button_snapshots)
        selectSnapshotButton?.setOnClickListener(onSnapshotButtonClickedListener)
        Log.d("Cata", "have toolbar!")
    }


    private fun initSwipeRefreshLayout(rootView: View) {
        swipeRefreshLayout = rootView.swiperefreshlayout_coin_lists
        swipeRefreshLayout.setOnRefreshListener { coinListPresenter.userPullToRefresh() }
    }

    private fun initRecyclerView(rootView: View, appCompatActivity: AppCompatActivity) {
        recyclerView = rootView.recyclerview_coins_list
        recyclerViewAdapter = CoinListAdapter(context = appCompatActivity.baseContext,
                coins = emptyList(),
                currency = coinListPresenter.getSelectedCurrency(),
                imageHelper = imageHelper) {
            coinListPresenter.coinSelected(it)
        }
        recyclerViewLayoutManager = LinearLayoutManager(appCompatActivity.baseContext)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = recyclerViewLayoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(scrolledRecyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                coinListPresenter.viewScrolled(recyclerView.computeVerticalScrollOffset(),
                        recyclerView.computeVerticalScrollRange())
            }
        })
    }

    private fun getListenerForDialogType(dialogType: CoinListSelectionDialogType): ListenerType {
        return when (dialogType) {
            CoinListSelectionDialogType.ChangeCurrency ->
                { item -> coinListPresenter.displayCurrencyChanged(item.toSelectionItem()) }
            CoinListSelectionDialogType.SelectSnapshot ->
                { item -> coinListPresenter.selectedSnapshotChanged(item.toSelectionItem()) }
        }
    }

    private fun showSelectionDialog(dialogType: CoinListSelectionDialogType, data: List<SelectionItem>) {
        val parcelableData = ArrayList(data.map { it.toParcelableSelectionItem() })
        val listener = getListenerForDialogType(dialogType = dialogType)
        val dialog = SelectionListDialog.Builder()
                .selectionListener(listener)
                .data(parcelableData)
                .build()
        dialog.show(fragmentManager, dialogType.typeName)
    }

    companion object {
        const val TAG = "CoinsListFragment"
    }
}