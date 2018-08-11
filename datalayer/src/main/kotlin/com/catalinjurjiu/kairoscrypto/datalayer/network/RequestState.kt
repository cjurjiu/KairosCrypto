package com.catalinjurjiu.kairoscrypto.datalayer.network

/**
 * Created by catalin on 07/04/2018.
 */
sealed class RequestState {
    object InFlight : RequestState()
    sealed class Idle : RequestState() {
        object NotStarted : Idle()
        sealed class Finished : Idle() {
            data class Error(val throwable: Throwable) : Finished()
            data class Success<out T>(val result: T) : Finished()
        }
    }
}