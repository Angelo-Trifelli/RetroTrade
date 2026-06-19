package com.example.retrotrade.ui.navigation.homepage

import com.example.retrotrade.model.ItemCategory
import com.example.retrotrade.model.RecentItem
import com.example.retrotrade.model.TrendingItem

data class HomeUiState(
    val username: String = "Collector",
    val collectionCount: Int = 0,
    val pendingTradesCount: Int = 0,
    val recentItems: List<RecentItem> = initDefaultRecentItems(),
    val trendingItems: List<TrendingItem> = initDefaultTrendingItems()
)

private fun initDefaultRecentItems(): List<RecentItem> {
    return listOf(
        RecentItem("Charizard Holo", ItemCategory.TRADING_CARDS,    "$285.00", "🔥"),
        RecentItem("Game Boy Color", ItemCategory.RETRO_GAMES,      "$120.00", "🎮"),
        RecentItem("Levi's 501 '89", ItemCategory.VINTAGE_CLOTHING, "$95.00",  "👖"),
        RecentItem("N64 Controller", ItemCategory.RETRO_GAMES,      "$45.00",  "🕹️"),
        RecentItem("Pikachu 1st Ed.", ItemCategory.TRADING_CARDS,    "$210.00", "⚡")
    )
}

private fun initDefaultTrendingItems(): List<TrendingItem> {
    return listOf(
        TrendingItem(
            "Super Nintendo Console", "Retro Games",
            "$175.00", "RetroMike", "2.4 km", "🎮"
        ),
        TrendingItem(
            "Blastoise Holo", "Trading Cards",
            "$195.00", "CardTrader99", "1.1 km", "💧"
        ),
        TrendingItem(
            "Vintage Nike Windbreaker", "Vintage Clothing",
            "$88.00", "ThriftQueen", "3.7 km", "🧥"
        )
    )
}