package com.catalinjurjiu.kairoscrypto.datalayer.network.cryptocompare.model

import com.google.gson.annotations.SerializedName


/*###################################################################################################
#####################################################################################################
############                                                                             ############
############ Data objects required to fetch a the first x coins by volume from           ############
############  Crypto Compare                                                             ############
############ "data/top/totalvol" endpoint                                                ############
############                                                                             ############
#####################################################################################################
#####################################################################################################*/

data class CryptoCompareCoinListByVolume(@SerializedName("Message")
                                         val message: String,
                                         @SerializedName("Type")
                                         val type: Int,
                                         @SerializedName("SponsoredData")
                                         val sponsoredData: List<CryptoCompareCryptoCoin>,
                                         @SerializedName("Data")
                                         val data: List<CryptoCompareCryptoCoin>,
                                         @SerializedName("HasWarning")
                                         val hasWarning: Boolean)

data class CryptoCompareCryptoCoin(@SerializedName("CoinInfo")
                                   val coinInfo: CoinInfo,
                                   @SerializedName("ConversionInfo")
                                   val conversionInfo: ConversionInfo)

data class CoinInfo(@SerializedName("Id")
                    val id: String,
                    @SerializedName("Name")
                    val name: String,
                    @SerializedName("FullName")
                    val fullName: String,
                    @SerializedName("Internal")
                    val internal: String,
                    @SerializedName("ImageUrl")
                    val imageUrl: String,
                    @SerializedName("Url")
                    val url: String,
                    @SerializedName("Algorithm")
                    val algorithm: String,
                    @SerializedName("ProofType")
                    val proofType: String,
                    @SerializedName("NetHashesPerSecond")
                    val netHashesPerSecond: Long,
                    @SerializedName("BlockNumber")
                    val blockNumber: Long,
                    @SerializedName("BlockTime")
                    val blockTime: Long,
                    @SerializedName("BlockReward")
                    val blockReward: Double,
                    @SerializedName("Type")
                    val type: Int,
                    @SerializedName("DocumentType")
                    val documentType: String)

data class ConversionInfo(@SerializedName("Conversion")
                          val conversion: String,
                          @SerializedName("ConversionSymbol")
                          val conversionSymbol: String,
                          @SerializedName("CurrencyFrom")
                          val currencyFrom: String,
                          @SerializedName("CurrencyTo")
                          val currencyTo: String,
                          @SerializedName("Market")
                          val market: String,
                          @SerializedName("Supply")
                          val supply: Long,
                          @SerializedName("TotalVolume24H")
                          val totalVolume24H: Double,
                          @SerializedName("RAW")
                          val raw: List<String>)