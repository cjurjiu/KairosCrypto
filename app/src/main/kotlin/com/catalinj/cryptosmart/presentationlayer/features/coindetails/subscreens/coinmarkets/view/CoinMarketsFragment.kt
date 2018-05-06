package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.di.components.CoinDetailsComponent
import com.catalinj.cryptosmart.di.components.CoinMarketsComponent
import com.catalinj.cryptosmart.di.modules.coindetails.subscreens.CoinMarketsModule
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract
import com.catalinjurjiu.smartpersist.DaggerFragment

/**
 * A simple view(fragment) which displays the exchanges on which a particular crypocurrency is
 * available for trade.
 */
class CoinMarketsFragment : DaggerFragment<CoinMarketsComponent>(), CoinMarketsContract.CoinMarketsView {
    override val name: String = TAG

    class Factory(private val coinId: String,
                  private val coinSymbol: String,
                  private val coinDetailsComponent: CoinDetailsComponent)

        : DaggerFragmentFactory<CoinMarketsComponent>() {
        override fun onCreateFragment(): DaggerFragment<CoinMarketsComponent> {
            val fragment = CoinMarketsFragment()
            val args = Bundle()
            args.putString(ARGS_COIN_ID, coinId)
            fragment.arguments = args
            return fragment
        }

        override fun onCreateDaggerComponent(): CoinMarketsComponent {
            return coinDetailsComponent.getCoinMarketsComponent(
                    coinMarketsModule = CoinMarketsModule(coinId)
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coin_markets, container, false)
    }

    override fun initialise() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPresenter(): CoinMarketsContract.CoinMarketsPresenter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val TAG = "CoinMarketsFragment"
        const val ARGS_COIN_ID = "ARGS::COIN_ID"
    }

}