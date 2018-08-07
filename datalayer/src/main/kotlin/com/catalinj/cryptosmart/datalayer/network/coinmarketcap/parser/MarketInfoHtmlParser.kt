package com.catalinj.cryptosmart.datalayer.network.coinmarketcap.parser

import android.util.Log
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinMarketInfo
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.PAIR_TABLE_COLUMN_INDEX
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.PRICE_TABLE_COLUMN_INDEX
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.RANK_TABLE_COLUMN_INDEX
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.SOURCE_TABLE_COLUMN_INDEX
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.UPDATED_TABLE_COLUMN_INDEX
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.VOLUME_24H_TABLE_COLUMN_INDEX
import com.catalinj.cryptosmart.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.VOLUME_PRC_TABLE_COLUMN_INDEX
import org.jsoup.Jsoup

class MarketInfoHtmlParser(private val coinSymbol: String, marketInfoHtmlPage: String) {

    //todo, provide a base URL
    private val marketInfoPage = Jsoup.parse(marketInfoHtmlPage)

    fun extractMarkets(): List<CoinMarketCapCryptoCoinMarketInfo> {
        //get the markets from the markets table
        val marketsTable = marketInfoPage.select("#markets-table")

//        //parse header data
//        val marketsTableHead = marketsTable.select("thead tr")[0].select("th")
        val marketsInfo = mutableListOf<CoinMarketCapCryptoCoinMarketInfo>()

        val marketsTableRows = marketsTable.select("tbody tr")
        for (row in marketsTableRows) {
            val columnData = row.select("td").toList()

            //rank
            val rank = columnData[RANK_TABLE_COLUMN_INDEX].text().trim().toInt()
            //exchange
            val source = columnData[SOURCE_TABLE_COLUMN_INDEX].select("a").text()
            //trading pair
            val tradingPairAnchor = columnData[PAIR_TABLE_COLUMN_INDEX].select("a")
            val tradingPairString = tradingPairAnchor.text().trim().split("/")
            val tradingPairUrl = tradingPairAnchor.attr("href").trim()
            //24h trading volume
            val tradingVol24HoursSpan = columnData[VOLUME_24H_TABLE_COLUMN_INDEX].select("span")
            val tradingVol24HoursUsd = tradingVol24HoursSpan.attr("data-usd").trim().toDouble()
            val tradingVol24HoursBtc = tradingVol24HoursSpan.attr("data-btc").trim().toDouble()
            //price data
            val priceDataSpan = columnData[PRICE_TABLE_COLUMN_INDEX].select("span")
            val priceDataUsd = priceDataSpan.attr("data-usd").trim().toDouble()
            val priceDataBtc = priceDataSpan.attr("data-btc").trim().toDouble()
            //volume percent data
            val volumePercentSpan = columnData[VOLUME_PRC_TABLE_COLUMN_INDEX].select("span")
            val volumePercent = volumePercentSpan.attr("data-format-value").trim().toFloat()
            //updated string
            val updatedString = columnData[UPDATED_TABLE_COLUMN_INDEX].text().trim()

            marketsInfo.add(CoinMarketCapCryptoCoinMarketInfo(rank = rank,
                    exchangeName = source,
                    exchangePairUrl = tradingPairUrl,
                    coinSymbol = coinSymbol,
                    exchangePairSymbol1 = tradingPairString[0],
                    exchangePairSymbol2 = tradingPairString[1],
                    volumeUsd = tradingVol24HoursUsd,
                    priceUsd = priceDataUsd,
                    volumePercent = volumePercent,
                    updatedFlag = updatedString,
                    lastUpdatedTimestamp = System.currentTimeMillis()))
        }
        Log.d("Cata", "PARSING FINISHED. FOUND ${marketsInfo.size} entries")
        return marketsInfo
    }

    object TableHeaderColumns {
        //header strings
        const val RANK_TABLE_COLUMN_INDEX = 0
        const val SOURCE_TABLE_COLUMN_INDEX = 1
        const val PAIR_TABLE_COLUMN_INDEX = 2
        const val VOLUME_24H_TABLE_COLUMN_INDEX = 3
        const val PRICE_TABLE_COLUMN_INDEX = 4
        const val VOLUME_PRC_TABLE_COLUMN_INDEX = 5
        const val UPDATED_TABLE_COLUMN_INDEX = 6
        //header strings
        const val RANK_TABLE_HEADER_STRING = "#"
        const val SOURCE_TABLE_HEADER_STRING = "Source"
        const val PAIR_TABLE_HEADER_STRING = "Pair"
        const val VOLUME_24H_TABLE_HEADER_STRING = "Volume (24h)"
        const val PRICE_TABLE_HEADER_STRING = "Price"
        const val VOLUME_PRC_TABLE_HEADER_STRING = "Volume (%)"
        const val UPDATED_TABLE_HEADER_STRING = "Updated"
        //ids
        const val SOURCE_TABLE_HEADER_ID = "th-source"
        const val PAIR_TABLE_HEADER_ID = "th-pair"

        val HEADERS_LIST = listOf(RANK_TABLE_HEADER_STRING,
                SOURCE_TABLE_HEADER_STRING,
                PAIR_TABLE_HEADER_STRING,
                VOLUME_24H_TABLE_HEADER_STRING,
                PRICE_TABLE_HEADER_STRING,
                VOLUME_PRC_TABLE_HEADER_STRING,
                UPDATED_TABLE_HEADER_STRING)
    }
}