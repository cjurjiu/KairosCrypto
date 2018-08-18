package com.catalinjurjiu.kairoscrypto.datalayer.network.abstractrequest

import com.catalinjurjiu.kairoscrypto.datalayer.network.RequestState
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject

/**
 * Created by catalin on 07/04/2018.
 */
abstract class ComposableApiRequest<T> {
    val state: Observable<RequestState>
    val response: Observable<T>
    val errors: Observable<Throwable>

    private val _trigger: PublishRelay<Int> = PublishRelay.create()

    init {
        val state: BehaviorSubject<RequestState> = BehaviorSubject.createDefault(RequestState.Idle.NotStarted)
        val response = ReplaySubject.createWithSize<T>(1)
        val errors = ReplaySubject.createWithSize<Throwable>(1)

        _trigger.observeOn(Schedulers.io())
                .doOnNext { state.onNext(RequestState.InFlight) }
                .flatMap { fetchData() }
                //success case: stop loading, call onNext & onComplete on the response observable.
                .doOnNext { successResult ->
                    state.onNext(RequestState.Idle.Finished.Success<T>(successResult))
                    state.onComplete()
                }
                .doOnNext {
                    response.onNext(it)
                    response.onComplete()
                    errors.onComplete()
                }
                //error case: stop loading, call onComplete on the response observable, send throwable
                //via the _errors relay
                .doOnError { error ->
                    state.onNext(RequestState.Idle.Finished.Error(error))
                    state.onComplete()
                }
                .doOnError { error ->
                    errors.onNext(error)
                    errors.onComplete()
                    response.onComplete()
                }
                .onErrorResumeNext(Observable.empty())
                .subscribe()

        this.state = state
        this.response = response
        this.errors = errors
    }

    protected abstract fun fetchData(): Observable<T>

    fun execute() {
        _trigger.accept(0)
    }
}