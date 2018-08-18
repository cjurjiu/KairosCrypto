package com.catalinjurjiu.kairoscrypto.businesslayer.repository

import com.catalinjurjiu.kairoscrypto.businesslayer.repository.Repository.LoadingState
import io.reactivex.Observable

/**
 * Interface definition for a Repository which exposes its internal loading state via an [Observable].
 *
 * @see LoadingState
 *
 * Created by catalinj on 28.01.2018.
 */
interface Repository {

    /**
     * Observable that notifies when the loading state of this Repository changes. This always
     * notifies with the most recent value, when an observer subscribes.
     *
     * Emits [LoadingState.Loading] when one or more requests are flight. Emits [LoadingState.Idle]
     * when all requests finish.
     *
     * Does not emit [LoadingState.Loading] every time a new request is started. Only emits when the
     * loading state changes.
     * */
    val loadingStateObservable: Observable<LoadingState>

    /**
     * Models the loading state of tha [Repository].
     *
     * @see Repository
     */
    sealed class LoadingState {

        /**
         * The repository is idle: no requests are in flight, no background work is being performed.
         */
        object Idle : LoadingState()

        /**
         * The repository is loading data through one or more requests.
         */
        object Loading : LoadingState()
    }
}