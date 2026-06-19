package com.example.retrotrade.ui.navigation.trades

import com.example.retrotrade.model.Trade
import com.example.retrotrade.model.TradeStatus

data class TradesUiState(
    val selectedTabIndex: Int = 0,
    val trades: List<Trade> = initDefaultTrades()
) {
    val filteredTrades: List<Trade>
        get() {
            val filtered = when (selectedTabIndex) {
                0 -> trades.filter { it.status != TradeStatus.COMPLETED && it.status != TradeStatus.REJECTED }
                1 -> trades.filter { it.status == TradeStatus.COMPLETED }
                2 -> trades.filter { it.status == TradeStatus.REJECTED }
                else -> { emptyList() }
            }

            return filtered
        }
}


private fun initDefaultTrades(): List<Trade> {
    return listOf(
        Trade(
            id = "a1",
            sellerName = "RetroMike",
            buyerName = "RetroMike",
            itemName = "N64 Gold Controller",
            itemIcon = "🎮",
            isBuying = true,
            requestedPrice = "$100.00",
            offeredPrice = "$90.00",
            status = TradeStatus.ACCEPTED,
            lastMessage = "Sounds good! Let's meet at the park.",
            timeAgo = "2h ago"
        ),
        Trade(
            id = "a2",
            sellerName = "VintageVault",
            buyerName = "VintageVault",
            itemName = "Nike Windbreaker '92",
            itemIcon = "🧥",
            isBuying = false,
            requestedPrice = "$80.00",
            offeredPrice = "$65.00",
            status = TradeStatus.PENDING,
            lastMessage = "Would you take $65 for it?",
            timeAgo = "5h ago"
        ),
        Trade(
            id = "a3",
            sellerName = "CardMaster",
            buyerName = "CardMaster",
            itemName = "Pikachu 1st Ed.",
            itemIcon = "⚡",
            isBuying = true,
            requestedPrice = "$450.00",
            offeredPrice = "$400.00",
            status = TradeStatus.REJECTED,
            lastMessage = "I can do $420 minimum.",
            timeAgo = "1d ago"
        )
    )
}