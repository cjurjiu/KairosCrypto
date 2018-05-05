package com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.presentationlayer.features.coindetails.subscreens.coininfo.contract.CoinInfoContract

/**
 * A simple view(fragment) which displays various information about a particular cryptocurrency.
 */
class CoinInfoFragment : Fragment(), CoinInfoContract.CoinInfoView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coin_info, container, false)
    }

    override fun initialise() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPresenter(): CoinInfoContract.CoinInfoPresenter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}