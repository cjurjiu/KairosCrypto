package com.catalinjurjiu.kairoscrypto.businesslayer.repository

import io.reactivex.Observable

/**
 * Interface definition for a Repository which exposes its internal loading state via an observable.
 * Created by catalinj on 28.01.2018.
 */
interface Repository {

    /**
     * Observable that notifies when the loading state of this Repository changes. This always
     * notifies with the most recent value, when an observer subscribes.
     */
    val loadingStateObservable: Observable<LoadingState>

    sealed class LoadingState {
        object Idle : LoadingState()
        object Loading : LoadingState()
    }
}