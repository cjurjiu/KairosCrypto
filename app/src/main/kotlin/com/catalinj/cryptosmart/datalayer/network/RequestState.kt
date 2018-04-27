package com.catalinj.cryptosmart.datalayer.network

/**
 * Created by catalin on 07/04/2018.
 */
sealed class RequestState {
    object Idle : RequestState()
    object Loading : RequestState()
    data class Error(val throwable: Throwable) : RequestState()
    data class Completed<T>(val result: T) : RequestState()
}