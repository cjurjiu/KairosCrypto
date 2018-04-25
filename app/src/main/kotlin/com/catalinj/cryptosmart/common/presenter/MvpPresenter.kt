package com.catalinj.cryptosmart.common.presenter

import com.catalinj.cryptosmart.common.view.MvpView

/**
 * Created by catalinj on 21.01.2018.
 */

interface MvpPresenter<P : MvpPresenter<P, V>, V : MvpView<P, V>> {

    fun startPresenting()

    fun stopPresenting()

    fun viewAvailable(view: V)

    fun viewInitialised()

    fun viewDestroyed()

    fun getView(): V?

}