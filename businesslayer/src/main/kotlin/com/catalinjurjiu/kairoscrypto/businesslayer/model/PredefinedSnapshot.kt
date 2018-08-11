package com.catalinjurjiu.kairoscrypto.businesslayer.model

/**
 * Represents a moment in the past at which a "snapshot" of the price data was taken.
 *
 * Useful to track changes in price since that moment in time.
 */
enum class PredefinedSnapshot(val stringValue: String) {
    SNAPSHOT_1H(stringValue = "snapshot_1h"),
    SNAPSHOT_24H(stringValue = "snapshot_24h"),
    SNAPSHOT_7D(stringValue = "snapshot_7d");

    companion object {
        /**
         * Returns the [PredefinedSnapshot] associated with the [String] value, or throws [IllegalArgumentException]
         * if the string does not represent a predefined snapshot.
         */
        fun of(string: String): PredefinedSnapshot = valueOf(string.toUpperCase())
    }
}

/**
 * Retrieves the change in price data over the period of time described by the [snapshot] parameter.
 */
fun PriceData.changeForSnapshot(snapshot: PredefinedSnapshot): Float {
    return when (snapshot) {
        PredefinedSnapshot.SNAPSHOT_1H -> percentChange1h
        PredefinedSnapshot.SNAPSHOT_24H -> percentChange24h
        PredefinedSnapshot.SNAPSHOT_7D -> percentChange7d
    }
}