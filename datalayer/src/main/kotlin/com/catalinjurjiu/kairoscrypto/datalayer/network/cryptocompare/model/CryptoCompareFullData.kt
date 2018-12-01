package com.catalinjurjiu.kairoscrypto.datalayer.network.cryptocompare.model

import com.google.gson.annotations.SerializedName

/*###################################################################################################
#####################################################################################################
############                                                                             ############
############ Data objects required to fetch a list of arbitrary coins from CryptoCompare ############
############ "/data/pricemultifull" endpoint                                             ############
############                                                                             ############
#####################################################################################################
#####################################################################################################*/

data class CryptoCompareMultiCoinFullDataStore(@SerializedName("RAW")
                                               val rawData: MultipleRawCoinsStore,
                                               @SerializedName("DISPLAY")
                                               val formattedCoinData: MultipleFormattedCoinsStore)

/**
 * Key of the map is the shorthand string for a crypto currency, value is a [SingleRawCoinCurrencyRepresentationStore].
 */
data class MultipleRawCoinsStore(val rawCoinsMap: Map<String, SingleRawCoinCurrencyRepresentationStore>)

/**
 * Given a crypto currency, stores value relative to various other currencies (either fiat, either other
 * cryptocurrencies)
 *
 * @param cryptoCurrencyValuesMap stores the value of a cryptocurrency relative to one or more other
 * currencies.
 * they key is a currency in which the value of the crypto coin is represented, the value is a \
 * [RawCoinData] which contains the actual value data.
 */
data class SingleRawCoinCurrencyRepresentationStore(val cryptoCurrencyValuesMap: Map<String, RawCoinData>)

/**
 * Key of the map is the shorthand string for a crypto currency, value is a [SingleRawCoinCurrencyRepresentationStore].
 */
data class MultipleFormattedCoinsStore(val rawCoinsMap: Map<String, SingleFormattedCoinCurrencyRepresentationStore>)

/**
 * Given a crypto currency, stores value relative to various other currencies (either fiat, either other
 * cryptocurrencies)
 *
 * @param cryptoCurrencyValuesMap stores the value of a cryptocurrency relative to one or more other
 * currencies.
 * they key is a currency in which the value of the crypto coin is represented, the value is a \
 * [RawCoinData] which contains the actual value data.
 */
data class SingleFormattedCoinCurrencyRepresentationStore(val cryptoCurrencyValuesMap: Map<String, FormattedCoinData>)

data class RawCoinData(@SerializedName("TYPE")
                       val type: String,
                       @SerializedName("MARKET")
                       val market: String,
                       @SerializedName("FROMSYMBOL")
                       val fromSymbol: String,
                       @SerializedName("TOSYMBOL")
                       val toSymbol: String,
                       @SerializedName("FLAGS")
                       val flags: String,
                       @SerializedName("PRICE")
                       val price: Double,
                       @SerializedName("LASTUPDATE")
                       val lastUpdate: Double,
                       @SerializedName("LASTVOLUME")
                       val lastVolume: Double,
                       @SerializedName("LASTVOLUMETO")
                       val lastVolumeTo: Double,
                       @SerializedName("LASTTRADEID")
                       val lastTradeId: String,
                       @SerializedName("VOLUMEDAY")
                       val volumeDay: Double,
                       @SerializedName("VOLUMEDAYTO")
                       val volumeDayTo: Double,
                       @SerializedName("VOLUME24HOUR")
                       val volume24Hour: Double,
                       @SerializedName("VOLUME24HOURTO")
                       val volume24HourTo: Double,
                       @SerializedName("OPENDAY")
                       val openDay: Double,
                       @SerializedName("HIGHDAY")
                       val highDay: Double,
                       @SerializedName("LOWDAY")
                       val lowDay: Double,
                       @SerializedName("OPEN24HOUR")
                       val open24Hour: Double,
                       @SerializedName("HIGH24HOUR")
                       val high24Hour: Double,
                       @SerializedName("LOW24HOUR")
                       val low24Hour: Double,
                       @SerializedName("LASTMARKET")
                       val lastMarket: String,
                       @SerializedName("CHANGE24HOUR")
                       val change24Hour: Double,
                       @SerializedName("CHANGEPCT24HOUR")
                       val changePct24Hour: Double,
                       @SerializedName("CHANGEDAY")
                       val changeDay: Double,
                       @SerializedName("CHANGEPCTDAY")
                       val changePctDay: Double,
                       @SerializedName("SUPPLY")
                       val supply: Double,
                       @SerializedName("MKTCAP")
                       val marketCap: Double,
                       @SerializedName("TOTALVOLUME24H")
                       val totalVolume24h: Double,
                       @SerializedName("TOTALVOLUME24HTO")
                       val totalVolume24hto: Double)

data class FormattedCoinData(@SerializedName("FROMSYMBOL")
                             val fromSymbol: String,
                             @SerializedName("TOSYMBOL")
                             val toSymbol: String,
                             @SerializedName("MARKET")
                             val market: String,
                             @SerializedName("PRICE")
                             val price: String,
                             @SerializedName("LASTUPDATE")
                             val lastUpdate: String,
                             @SerializedName("LASTVOLUME")
                             val lastVolume: String,
                             @SerializedName("LASTVOLUMETO")
                             val lastVolumeto: String,
                             @SerializedName("LASTTRADEID")
                             val lastTradeid: String,
                             @SerializedName("VOLUMEDAY")
                             val volumeDay: String,
                             @SerializedName("VOLUMEDAYTO")
                             val volumeDayTo: String,
                             @SerializedName("VOLUME24HOUR")
                             val volume24Hour: String,
                             @SerializedName("VOLUME24HOURTO")
                             val volume24HourTo: String,
                             @SerializedName("OPENDAY")
                             val openDay: String,
                             @SerializedName("HIGHDAY")
                             val highDay: String,
                             @SerializedName("LOWDAY")
                             val lowDay: String,
                             @SerializedName("OPEN24HOUR")
                             val open24Hour: String,
                             @SerializedName("HIGH24HOUR")
                             val high24Hour: String,
                             @SerializedName("LOW24HOUR")
                             val low24Hour: String,
                             @SerializedName("LASTMARKET")
                             val lastMarket: String,
                             @SerializedName("CHANGE24HOUR")
                             val change24Hour: String,
                             @SerializedName("CHANGEPCT24HOUR")
                             val changePct24Hour: String,
                             @SerializedName("CHANGEDAY")
                             val changeDay: String,
                             @SerializedName("CHANGEPCTDAY")
                             val changePctDay: String,
                             @SerializedName("SUPPLY")
                             val supply: String,
                             @SerializedName("MKTCAP")
                             val marketCap: String,
                             @SerializedName("TOTALVOLUME24H")
                             val totalVolume24Hours: String,
                             @SerializedName("TOTALVOLUME24HTO")
                             val totalVolume24HoursTo: String)