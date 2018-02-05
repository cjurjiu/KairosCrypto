package com.catalinj.cryptosmart.common.view

import com.catalinj.cryptosmart.common.presenter.MvpPresenter

/**
 * Created by catalinj on 21.01.2018.
 */

interface MvpView<P : MvpPresenter<P, V>, V : MvpView<P, V>> {
    fun getPresenter(): P
}
