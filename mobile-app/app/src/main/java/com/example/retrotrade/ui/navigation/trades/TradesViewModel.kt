package com.example.retrotrade.ui.navigation.trades

import com.example.retrotrade.ui.navigation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ─── Trade suggestion model ────────────────────────────────────────
data class TradeSuggestion(
    val id: String,
    val itemName: String,
    val itemCategory: String,
    val marketValue: String,
    val requestedPrice: String,
    val itemIcon: String,
    val sellerName: String,
    val sellerDistance: String,
    val matchPercentage: Int
)

// ─── Active trade model ─────────────────────────────────────────────
data class ActiveTrade(
    val id: String,
    val partnerName: String,
    val itemName: String,
    val itemIcon: String,
    val isBuying: Boolean,
    val requestedPrice: String,
    val offeredPrice: String,
    val status: TradeStatus,
    val lastMessage: String,
    val timeAgo: String
)

enum class TradeStatus(val label: String) {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    COUNTER_OFFER("Counter Offer"),
    COMPLETED("Completed"),
    DECLINED("Declined")
}

// ─── UI State ───────────────────────────────────────────────────────
data class TradesUiState(
    val selectedTabIndex: Int = 0,
    val suggestions: List<TradeSuggestion> = initDefaultSuggestions(),
    val activeTrades: List<ActiveTrade> = initDefaultActiveTrades()
)

class TradesViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow(TradesUiState())
    val uiState: StateFlow<TradesUiState> = _uiState.asStateFlow()

    fun onTabSelected(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = index)
    }
}


// ─── Mock data ──────────────────────────────────────────────────────
private fun initDefaultSuggestions(): List<TradeSuggestion> {
    return listOf(
        TradeSuggestion(
            id = "1",
            itemName = "Charizard Base Set",
            itemCategory = "Trading Cards",
            marketValue = "$310.00",
            requestedPrice = "$295.00",
            itemIcon = "🔥",
            sellerName = "CardTrader99",
            sellerDistance = "1.1 km",
            matchPercentage = 94
        ),
        TradeSuggestion(
            id = "2",
            itemName = "SNES Console",
            itemCategory = "Retro Games",
            marketValue = "$175.00",
            requestedPrice = "$150.00",
            itemIcon = "🎮",
            sellerName = "RetroMike",
            sellerDistance = "2.4 km",
            matchPercentage = 87
        ),
        TradeSuggestion(
            id = "3",
            itemName = "Vintage Levi's Jacket",
            itemCategory = "Vintage Clothing",
            marketValue = "$130.00",
            requestedPrice = "$135.00",
            itemIcon = "🧥",
            sellerName = "ThriftQueen",
            sellerDistance = "3.7 km",
            matchPercentage = 78
        )
    )
}

private fun initDefaultActiveTrades(): List<ActiveTrade> {
    return listOf(
        ActiveTrade(
            id = "a1",
            partnerName = "RetroMike",
            itemName = "N64 Gold Controller",
            itemIcon = "🎮",
            isBuying = true,
            requestedPrice = "$100.00",
            offeredPrice = "$90.00",
            status = TradeStatus.ACCEPTED,
            lastMessage = "Sounds good! Let's meet at the park.",
            timeAgo = "2h ago"
        ),
        ActiveTrade(
            id = "a2",
            partnerName = "VintageVault",
            itemName = "Nike Windbreaker '92",
            itemIcon = "🧥",
            isBuying = false,
            requestedPrice = "$80.00",
            offeredPrice = "$65.00",
            status = TradeStatus.PENDING,
            lastMessage = "Would you take $65 for it?",
            timeAgo = "5h ago"
        ),
        ActiveTrade(
            id = "a3",
            partnerName = "CardMaster",
            itemName = "Pikachu 1st Ed.",
            itemIcon = "⚡",
            isBuying = true,
            requestedPrice = "$450.00",
            offeredPrice = "$400.00",
            status = TradeStatus.COUNTER_OFFER,
            lastMessage = "I can do $420 minimum.",
            timeAgo = "1d ago"
        )
    )
}
