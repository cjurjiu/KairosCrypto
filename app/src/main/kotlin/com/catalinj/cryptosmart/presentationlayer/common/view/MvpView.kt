package com.catalinj.cryptosmart.presentationlayer.common.view

import com.catalinj.cryptosmart.presentationlayer.common.presenter.MvpPresenter

/**
 * Created by catalinj on 21.01.2018.
 */

interface MvpView<P : MvpPresenter<P, V>, V : MvpView<P, V>> {

    fun initialise()

    fun getPresenter(): P
}
