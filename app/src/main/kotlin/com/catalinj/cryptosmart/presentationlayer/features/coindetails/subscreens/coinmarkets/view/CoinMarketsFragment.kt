package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coinmarkets.contract.CoinMarketsContract

/**
 * A simple view(fragment) which displays the exchanges on which a particular crypocurrency is
 * available for trade.
 */
class CoinMarketsFragment : Fragment(), CoinMarketsContract.CoinMarketsView {

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

}