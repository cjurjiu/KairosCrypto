package com.catalinj.cryptosmart.features.coindetails.view


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.common.functional.BackEventAwareComponent
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.components.CoinDetailsComponent
import com.catalinj.cryptosmart.di.modules.coindetails.CoinDetailsModule
import com.catalinj.cryptosmart.features.coinslist.contract.CoinDetailsContract
import com.catalinj.cryptosmart.network.coinmarketcap.CoinMarketCapCryptoCoin
import com.catalinjurjiu.common.NamedComponent
import com.catalinjurjiu.smartpersist.DaggerFragment
import javax.inject.Inject


class CoinDetailsFragment : DaggerFragment<CoinDetailsComponent>(), NamedComponent, BackEventAwareComponent,
        CoinDetailsContract.CoinDetailsView {

    override val name: String = TAG
    @Inject
    protected lateinit var coinDetailsPresenter: CoinDetailsContract.CoinDetailsPresenter
    private var mPlusOneButton: Button? = null

    class Factory(private val activityComponent: ActivityComponent) : DaggerFragmentFactory<CoinDetailsComponent>() {

        override fun onCreateFragment(): DaggerFragment<CoinDetailsComponent> {
            val f = CoinDetailsFragment()
            //do some other initializations, set arguments
            return f
        }

        override fun onCreateDaggerComponent(): CoinDetailsComponent {
            return activityComponent.getCoinDetailsComponent(CoinDetailsModule())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        Log.d(TAG, "CoinDetailsFragment${hashCode().toString(16)}#onCreate.injector:" + injector.hashCode().toString(16) + " presenter:" + coinDetailsPresenter.hashCode().toString(16))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d(TAG, "CoinDetailsFragment#onCreateView")
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_plus_one, container, false)

        //Find the +1 button
        mPlusOneButton = view.findViewById<View>(R.id.plus_one_button) as Button
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Log.d(TAG, "CoinDetailsFragment#onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        coinDetailsPresenter.onViewAvailable(this)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "CoinDetailsFragment#onStart")
        coinDetailsPresenter.startPresenting()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "CoinDetailsFragment#onResume")
        getPresenter().receivedFocus()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "CoinDetailsFragment#onStop")
        coinDetailsPresenter.stopPresenting()
        Log.d(TAG, "CoinDetailsFragment#onStop. isRemoving:$isRemoving isActivityFinishing:${activity.isFinishing} " +
                "a2:${activity.isChangingConfigurations}")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "CoinDetailsFragment#onSaveInstanceState. isRemoving:$isRemoving isActivityFinishing:${activity.isFinishing} " +
                "a2:${activity.isChangingConfigurations}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "CoinDetailsFragment#onDestroyView")
        coinDetailsPresenter.onViewDestroyed()
        Log.d(TAG, "CoinDetailsFragment#onDestroyView. isRemoving:$isRemoving isActivityFinishing:${activity.isFinishing} " +
                "a2:${activity.isChangingConfigurations}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "CoinDetailsFragment#onDestroy")
        //TODO release presenter reference?
        Log.d(TAG, "CoinDetailsFragment#onDestroy. isRemoving:$isRemoving isActivityFinishing:${activity.isFinishing} " +
                "a2:${activity.isChangingConfigurations}")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "CoinDetailsFragment#onDetach")
    }

    override fun onBack(): Boolean {
        Log.d("Cata", "CoinDetailsFragment back pressed")
        return false
    }

    override fun getPresenter(): CoinDetailsContract.CoinDetailsPresenter {
        return coinDetailsPresenter
    }

    override fun setCoinData(data: CoinMarketCapCryptoCoin) {
        //todo
    }

    override fun showLoadingIndicator() {
        //todo
    }

    override fun hideLoadingIndicator() {
        //todo
    }

    override fun increaseValue() {
        // Refresh the state of the +1 button each time the activity receives focus.
        Log.d("Cata", "buttonText: ${mPlusOneButton!!}")
    }

    internal companion object {
        const val TAG: String = "CoinDetailsFragment"
    }
}
