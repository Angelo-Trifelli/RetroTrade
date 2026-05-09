package com.example.retrotrade.ui.navigation.homepage

import com.example.retrotrade.firebaseAuth
import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CollectionItem(
    val name: String,
    val category: String,
    val estimatedValue: String,
    val iconChar: String
)

data class TrendingItem(
    val name: String,
    val category: String,
    val estimatedValue: String,
    val ownerName: String,
    val distance: String,
    val iconChar: String
)

data class HomeUiState(
    val username: String = "Collector",
    val collectionCount: Int = 0,
    val pendingTradesCount: Int = 0,
    val totalEstimatedValue: String = "$0",
    val recentItems: List<CollectionItem> = emptyList(),
    val trendingItems: List<TrendingItem> = emptyList()
)

class HomeViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        val displayName = firebaseAuth.currentUser?.displayName
        val username = if (!displayName.isNullOrBlank()) displayName else "Collector"

        val recentItems = listOf(
            CollectionItem("Charizard Holo", "Trading Cards", "$285.00", "🔥"),
            CollectionItem("Game Boy Color", "Retro Games", "$120.00", "🎮"),
            CollectionItem("Levi's 501 '89", "Vintage Clothing", "$95.00", "👖"),
            CollectionItem("N64 Controller", "Retro Games", "$45.00", "🕹️"),
            CollectionItem("Pikachu 1st Ed.", "Trading Cards", "$210.00", "⚡")
        )

        val trendingItems = listOf(
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

        _uiState.value = HomeUiState(
            username = username,
            collectionCount = 24,
            pendingTradesCount = 3,
            totalEstimatedValue = "$1,847",
            recentItems = recentItems,
            trendingItems = trendingItems
        )
    }

    fun onScanClicked() {
        navigate(Screen.Scan.route)
    }

    fun onCollectionClicked() {
        navigate(Screen.Collection.route)
    }

    fun onTradesClicked() {
        navigate(Screen.Trades.route)
    }

    fun onChatClicked() {
        // Will navigate to chat when implemented
    }
}
