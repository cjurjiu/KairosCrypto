package com.catalinjurjiu.kairoscrypto.presentationlayer.common.view

import com.catalinjurjiu.kairoscrypto.businesslayer.model.PredefinedSnapshot
import com.catalinjurjiu.kairoscrypto.datalayer.CurrencyRepresentation

/**
 * Immutable settings for the adapter.
 */
data class CryptoListAdapterSettings(val currency: CurrencyRepresentation, val snapshot: PredefinedSnapshot) {

    /**
     * Returns a new [Settings] instance with updated currency.
     */
    fun updateCurrency(newCurrency: CurrencyRepresentation) = CryptoListAdapterSettings(currency = newCurrency, snapshot = this.snapshot)

    /**
     * Returns a new [Settings] instance with updated delta time.
     */
    fun updateDeltaTime(newSnapshot: PredefinedSnapshot) = CryptoListAdapterSettings(currency = this.currency, snapshot = newSnapshot)
}