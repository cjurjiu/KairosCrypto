package com.catalinj.cryptosmart.datalayer.network

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject

/**
 * Created by catalin on 07/04/2018.
 */
abstract class SimpleApiRequest<T> {
    val state: Observable<RequestState>
    val observable: Observable<T>

    private val _trigger: PublishRelay<Int> = PublishRelay.create()

    init {
        val state: BehaviorSubject<RequestState> = BehaviorSubject.createDefault(RequestState.Idle.NotStarted)
        val observable = ReplaySubject.createWithSize<T>(1)

        _trigger.observeOn(Schedulers.io())
                .doOnNext { state.onNext(RequestState.InFlight) }
                .flatMap { fetchData() }
                //success case: stop loading, call onNext & onComplete on the response observable.
                .doOnNext { successResult ->
                    state.onNext(RequestState.Idle.Finished.Success<T>(successResult))
                    state.onComplete()
                }
                .doOnNext {
                    observable.onNext(it)
                    observable.onComplete()
                }
                //error case: stop loading, call onComplete on the response observable, send throwable
                //via the _errors relay
                .doOnError { error ->
                    state.onNext(RequestState.Idle.Finished.Error(error))
                    state.onComplete()
                }
                .doOnError { error ->
                    observable.onError(error)
                }
                .subscribe()

        this.state = state
        this.observable = observable
    }

    abstract fun fetchData(): Observable<T>

    fun execute() {
        _trigger.accept(0)
    }
}