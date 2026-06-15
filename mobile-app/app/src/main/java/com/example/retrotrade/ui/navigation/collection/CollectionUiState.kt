package com.example.retrotrade.ui.navigation.collection

import com.example.retrotrade.model.CollectionListItem
import com.example.retrotrade.model.ItemCategory
import com.example.retrotrade.model.ItemStatus
import com.example.retrotrade.ui.screens.collection.CollectionFilter

data class CollectionUiState(
    val searchQuery: String = "",
    val selectedFilter: CollectionFilter = CollectionFilter.ALL,
    val items: List<CollectionListItem> = initDefaultListItems()
) {
    val filteredItems: List<CollectionListItem>
        get() {
            val filtered = when (selectedFilter) {
                CollectionFilter.ALL -> items
                CollectionFilter.ACTIVE -> items.filter { it.status == ItemStatus.ACTIVE }
                CollectionFilter.PENDING -> items.filter { it.status == ItemStatus.PENDING }
                CollectionFilter.SOLD -> items.filter { it.status == ItemStatus.SOLD }
            }

            return if (searchQuery.isBlank())
                filtered
            else
                filtered.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                    it.category.label.contains(searchQuery, ignoreCase = true)
                }
        }
}


// ─── Mock data ──────────────────────────────────────────────────────
private fun initDefaultListItems(): List<CollectionListItem> {
    return listOf(
        CollectionListItem(
            id = "1",
            name = "Charizard Holo",
            category = ItemCategory.TRADING_CARDS,
            estimatedValue = "$285.00",
            iconChar = "🔥",
            status = ItemStatus.ACTIVE
        ),
        CollectionListItem(
            id = "2",
            name = "Game Boy Color",
            category = ItemCategory.RETRO_GAMES,
            estimatedValue = "$120.00",
            iconChar = "🎮",
            status = ItemStatus.ACTIVE
        ),
        CollectionListItem(
            id = "3",
            name = "Levi's 501 '89",
            category = ItemCategory.VINTAGE_CLOTHING,
            estimatedValue = "$95.00",
            iconChar = "👖",
            status = ItemStatus.PENDING
        ),
        CollectionListItem(
            id = "4",
            name = "N64 Controller",
            category = ItemCategory.RETRO_GAMES,
            estimatedValue = "$45.00",
            iconChar = "🕹️",
            status = ItemStatus.ACTIVE
        ),
        CollectionListItem(
            id = "5",
            name = "Pikachu 1st Ed.",
            category = ItemCategory.TRADING_CARDS,
            estimatedValue = "$210.00",
            iconChar = "⚡",
            status = ItemStatus.SOLD
        ),
        CollectionListItem(
            id = "6",
            name = "SNES Console",
            category = ItemCategory.RETRO_GAMES,
            estimatedValue = "$175.00",
            iconChar = "🎮",
            status = ItemStatus.ACTIVE
        ),
        CollectionListItem(
            id = "7",
            name = "Vintage Nike Windbreaker",
            category = ItemCategory.VINTAGE_CLOTHING,
            estimatedValue = "$88.00",
            iconChar = "🧥",
            status = ItemStatus.PENDING
        )
    )
}