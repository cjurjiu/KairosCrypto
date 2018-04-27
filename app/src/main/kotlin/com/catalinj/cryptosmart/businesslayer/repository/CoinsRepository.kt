package com.catalinj.cryptosmart.businesslayer.repository

import com.catalinj.cryptosmart.datalayer.network.RequestState
import com.catalinj.cryptosmart.businesslayer.model.CryptoCoin
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Interface definition for a Repository capable of providing & managing [CryptoCoin] objects.
 * Created by catalinj on 28.01.2018.
 */
interface CoinsRepository {

    /**
     * Observable that notifies when the available list of [CryptoCoin]s has been updated.
     */
    val cryptoCoinObservable: Observable<List<CryptoCoin>>

    /**
     * Observable that notifies when the loading state of this Repository changes. This always
     * notifies with the most recent value, when an observer subscribes.
     */
    val loadingStateObservable: Observable<RequestState>

    /**
     * Updates the known list of coins.
     *
     * Once the coins have been updated, the [cryptoCoinObservable] will notify its observers.
     * @param startIndex the index of the first coin in the list
     * @param numberOfCoins the number of coins to fetch
     * @param errorHandler consumer which will be notified if an error happens
     */
    fun fetchCoins(startIndex: Int, numberOfCoins: Int, errorHandler: Consumer<Throwable>)
}