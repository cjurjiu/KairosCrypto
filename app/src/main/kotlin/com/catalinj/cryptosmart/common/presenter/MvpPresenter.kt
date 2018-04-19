package com.catalinj.cryptosmart.common.presenter

import com.catalinj.cryptosmart.common.view.MvpView

/**
 * Created by catalinj on 21.01.2018.
 */

interface MvpPresenter<P : MvpPresenter<P, V>, V : MvpView<P, V>> {

    fun startPresenting()

    fun stopPresenting()

    fun onViewAvailable(view: V)

    fun onViewDestroyed()

    fun getView(): V?

}

//interface MvpPresenter<P : MvpPresenter<P, V>, V : MvpView<P, V>> {
//    fun attachView(v: V)
//    fun detachView()
//}
