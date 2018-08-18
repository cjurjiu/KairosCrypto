package com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.parser

import android.util.Log
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.model.CoinMarketCapCryptoCoinMarketInfo
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.PAIR_TABLE_COLUMN_INDEX
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.PRICE_TABLE_COLUMN_INDEX
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.RANK_TABLE_COLUMN_INDEX
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.SOURCE_TABLE_COLUMN_INDEX
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.UPDATED_TABLE_COLUMN_INDEX
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.VOLUME_24H_TABLE_COLUMN_INDEX
import com.catalinjurjiu.kairoscrypto.datalayer.network.coinmarketcap.parser.MarketInfoHtmlParser.TableHeaderColumns.VOLUME_PRC_TABLE_COLUMN_INDEX
import org.jsoup.Jsoup

/**
 * Interprets the HTML of a CoinMarketCap page which shows markets data for a coin into a list of
 * [CoinMarketCapCryptoCoinMarketInfo] objects.
 *
 * This is achieved when [extractMarkets] is invoked.
 *
 * @constructor Creates a new instance of the [MarketInfoHtmlParser].
 *
 * When the constructor is invoked, the HTML stored in [marketInfoPage] will be parsed synchronously
 * into a [Document][org.jsoup.nodes.Document]. Invoke the constructor form a background thread to
 * prevent blocking the main thread.
 *
 * @param coinSymbol The symbol of the coin for which market data is interpreted.
 * @param marketInfoHtmlPage String which contains the HTML of the page that stores the market data
 * for the targeted coin.
 */
class MarketInfoHtmlParser(private val coinSymbol: String, marketInfoHtmlPage: String) {

    private val marketInfoPage = Jsoup.parse(marketInfoHtmlPage)

    fun extractMarkets(): List<CoinMarketCapCryptoCoinMarketInfo> {
        //get the markets from the markets table
        val marketsTable = marketInfoPage.select("#markets-table")

        //parse header data
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
        Log.d("MarketsInfoParser", "Parsing finished. Found: ${marketsInfo.size} entries.")
        return marketsInfo
    }

    object TableHeaderColumns {
        //header indexes
        const val RANK_TABLE_COLUMN_INDEX = 0
        const val SOURCE_TABLE_COLUMN_INDEX = 1
        const val PAIR_TABLE_COLUMN_INDEX = 2
        const val VOLUME_24H_TABLE_COLUMN_INDEX = 3
        const val PRICE_TABLE_COLUMN_INDEX = 4
        const val VOLUME_PRC_TABLE_COLUMN_INDEX = 5
        const val UPDATED_TABLE_COLUMN_INDEX = 6
    }
}