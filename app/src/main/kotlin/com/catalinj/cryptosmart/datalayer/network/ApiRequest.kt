package com.catalinj.cryptosmart.datalayer.network

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by catalin on 07/04/2018.
 */
abstract class ApiRequest<T> {
    val state: Observable<RequestState>
        get() {
            return _state
        }

    val response: Observable<T>
        get() {
            return _response
        }

    val errors: Observable<Throwable>
        get() {
            return _errors
        }

    private val _state: BehaviorRelay<RequestState> = BehaviorRelay.createDefault(RequestState.Idle)
    private val _response = BehaviorSubject.create<T>()
    private val _errors = BehaviorRelay.create<Throwable>()
    private val _trigger: BehaviorRelay<Int> = BehaviorRelay.create()

    init {
        _trigger.doOnNext { _state.accept(RequestState.Loading) }
                .observeOn(Schedulers.io())
                .flatMap { fetchData() }
                //success case: stop loading, call onNext & onComplete on the response observable.
                .doOnNext { _state.accept(RequestState.Completed<T>(it)) }
                .doOnNext {
                    _response.onNext(it)
                    _response.onComplete()
                }
                //error case: stop loading, call onComplete on the response observable, send throwable
                //via the _errors relay
                .doOnError { _state.accept(RequestState.Error(it)) }
                .doOnError { _errors.accept(it) }
                .onErrorResumeNext(Observable.empty())
                .subscribe()
    }

    abstract fun fetchData(): Observable<T>

    fun execute() {
        _trigger.accept(0)
    }
}