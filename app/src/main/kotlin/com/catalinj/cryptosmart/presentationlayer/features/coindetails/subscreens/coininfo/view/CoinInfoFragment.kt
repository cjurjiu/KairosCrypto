package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoinDetails
import com.catalinj.cryptosmart.businesslayer.model.ErrorCode
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.di.components.CoinDetailsComponent
import com.catalinj.cryptosmart.di.components.CoinInfoComponent
import com.catalinj.cryptosmart.di.modules.coindetails.subscreens.CoinInfoModule
import com.catalinj.cryptosmart.presentationlayer.common.extension.displayPercent
import com.catalinj.cryptosmart.presentationlayer.common.extension.toMessageResId
import com.catalinj.cryptosmart.presentationlayer.common.formatter.CurrencyFormatter
import com.catalinj.cryptosmart.presentationlayer.common.threading.Executors
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.contract.CoinInfoContract
import com.catalinj.cryptosmart.presentationlayer.features.widgets.snackbar.SnackBarWrapper
import com.catalinjurjiu.wheelbarrow.WheelbarrowFragment
import kotlinx.android.synthetic.main.layout_fragment_coin_info.view.*
import javax.inject.Inject

/**
 * A simple view(fragment) which displays various information about a particular cryptocurrency.
 */
class CoinInfoFragment : WheelbarrowFragment<CoinInfoComponent>(),
        CoinInfoContract.CoinInfoView {

    override val name: String = TAG

    @Inject
    protected lateinit var coinInfoPresenter: CoinInfoContract.CoinInfoPresenter

    private lateinit var selectPeriodCoinInfo24HButton: Button
    private lateinit var selectPeriodCoinInfo7DButton: Button
    private lateinit var selectPeriodCoinInfo1MButton: Button
    private lateinit var selectPeriodCoinInfo3MButton: Button
    private lateinit var unitTimePeriodChangeTextView: TextView
    private lateinit var unitValuePrimaryCurrencyTextView: TextView
    private lateinit var percentValueChangePrimaryCurrencyTextView: TextView
    private lateinit var unitValueBtcTextView: TextView
    private lateinit var percentValueChangeBtcTextView: TextView
    private lateinit var marketCapPrimaryCurrencyTextView: TextView
    private lateinit var marketCapBtcTextView: TextView
    private lateinit var volumePrimaryCurrencyTextView: TextView
    private lateinit var volumeBtcTextView: TextView
    private lateinit var circulatingSupplyValueTextView: TextView
    private lateinit var currentlyAvailableSupplyValueTextView: TextView
    private lateinit var maxPossibleSupplyValueTextView: TextView

    class Factory(private val coinId: String,
                  private val coinSymbol: String,
                  private val coinDetailsComponent: CoinDetailsComponent)
        : WheelbarrowFragment.Factory<CoinInfoComponent>() {

        override fun onCreateFragment(): WheelbarrowFragment<CoinInfoComponent> {
            val fragment = CoinInfoFragment()
            val args = Bundle()
            args.putString(ARGS_COIN_ID, coinId)
            args.putString(ARGS_COIN_SYMBOL, coinSymbol)
            fragment.arguments = args
            return fragment
        }

        override fun onCreateCargo(): CoinInfoComponent {
            return coinDetailsComponent.getCoinInfoComponent(coinInfoModule = CoinInfoModule())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cargo.inject(coinInfoFragment = this)
        val coinId = arguments!!.getString(ARGS_COIN_ID)
        val coinSymbol = arguments!!.getString(ARGS_COIN_SYMBOL)
        coinInfoPresenter.setCoinId(coinId = coinId)
        coinInfoPresenter.setCoinSymbol(coinSymbol = coinSymbol)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_fragment_coin_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinInfoPresenter.viewAvailable(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coinInfoPresenter.viewDestroyed()
    }

    override fun initialise() {
        val view = view!!
        //todo set listeners to buttons
        selectPeriodCoinInfo24HButton = view.button_select_period_coin_info_24H
        selectPeriodCoinInfo7DButton = view.button_select_period_coin_info_7D
        selectPeriodCoinInfo1MButton = view.button_select_period_coin_info_1M
        selectPeriodCoinInfo3MButton = view.button_select_period_coin_info_3M
        //text views
        unitTimePeriodChangeTextView = view.text_unit_time_period_change
        unitValuePrimaryCurrencyTextView = view.text_unit_value_primary_currency
        percentValueChangePrimaryCurrencyTextView = view.text_percent_value_change_primary_currency
        unitValueBtcTextView = view.text_unit_value_btc
        percentValueChangeBtcTextView = view.text_percent_value_change_btc
        marketCapPrimaryCurrencyTextView = view.text_market_cap_primary_currency
        marketCapBtcTextView = view.text_market_cap_btc
        volumePrimaryCurrencyTextView = view.text_volume_primary_currency
        volumeBtcTextView = view.text_volume_btc
        circulatingSupplyValueTextView = view.text_circulating_supply_value
        currentlyAvailableSupplyValueTextView = view.text_currently_available_supply_value
        maxPossibleSupplyValueTextView = view.text_max_possible_supply_value

    }

    override fun getPresenter(): CoinInfoContract.CoinInfoPresenter {
        return coinInfoPresenter
    }

    override fun onStart() {
        super.onStart()
        coinInfoPresenter.startPresenting()
    }

    override fun onStop() {
        super.onStop()
        coinInfoPresenter.stopPresenting()
    }

    override fun setCoinInfo(coinDetails: CryptoCoinDetails) {

        if (coinDetails.priceData.isEmpty()) {
            Log.e("Cata", "Price data empty??")
            //ignore
            return
        } else {
            Log.e("Cata", "Price data contains currencies:${coinDetails.priceData.keys}")
        }

        val primaryCurrencyPriceData = coinDetails.priceData[CurrencyRepresentation.USD.currency]!!
        val bitcoinPriceData = coinDetails.priceData[CurrencyRepresentation.BTC.currency]!!

        unitTimePeriodChangeTextView.text = "7d"

        unitValueBtcTextView.text = CurrencyFormatter.format(value = bitcoinPriceData.price,
                currencyRepresentation = CurrencyRepresentation.BTC)

        percentValueChangeBtcTextView.displayPercent(bitcoinPriceData.percentChange24h)

        marketCapBtcTextView.text = CurrencyFormatter.format(value = bitcoinPriceData.marketCap,
                currencyRepresentation = CurrencyRepresentation.BTC)
        volumeBtcTextView.text = CurrencyFormatter.format(value = bitcoinPriceData.volume24h,
                currencyRepresentation = CurrencyRepresentation.BTC)

        unitValuePrimaryCurrencyTextView.text = CurrencyFormatter.format(value = primaryCurrencyPriceData.price,
                currencyRepresentation = coinInfoPresenter.getPrimaryCurrency())

        percentValueChangePrimaryCurrencyTextView.displayPercent(primaryCurrencyPriceData.percentChange24h)

        marketCapPrimaryCurrencyTextView.text = CurrencyFormatter.format(value = primaryCurrencyPriceData.marketCap,
                currencyRepresentation = coinInfoPresenter.getPrimaryCurrency())
        volumePrimaryCurrencyTextView.text = CurrencyFormatter.format(value = primaryCurrencyPriceData.volume24h,
                currencyRepresentation = coinInfoPresenter.getPrimaryCurrency())

        circulatingSupplyValueTextView.text = CurrencyFormatter.formatCrypto(value = coinDetails.circulatingSupply, symbol = coinDetails.symbol)
        currentlyAvailableSupplyValueTextView.text = CurrencyFormatter.formatCrypto(value = coinDetails.totalSupply, symbol = coinDetails.symbol)
        maxPossibleSupplyValueTextView.text = CurrencyFormatter.formatCrypto(value = coinDetails.maxSupply, symbol = coinDetails.symbol)
    }

    override fun showError(errorCode: ErrorCode, retryHandler: () -> Unit) {
        Executors.mainThread().execute {
            SnackBarWrapper.showSnackBarWithAction(view = view!!,
                    infoMessageRes = errorCode.toMessageResId(),
                    actionMessageRes = R.string.cta_try_again,
                    clickListener = View.OnClickListener {
                        retryHandler.invoke()
                    })
        }
    }

    companion object {
        const val TAG = "CoinInfoFragment"
        const val ARGS_COIN_ID = "ARGS::COIN_ID"
        const val ARGS_COIN_SYMBOL = "ARGS::COIN_SYMBOL"
    }
}