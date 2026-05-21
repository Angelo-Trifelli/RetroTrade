package com.example.retrotrade.ui.navigation.collection

import com.example.retrotrade.ui.navigation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ─── Collection item model ──────────────────────────────────────────
data class CollectionListingItem(
    val id: String,
    val name: String,
    val category: String,
    val estimatedValue: String,
    val iconChar: String,
    val status: ListingStatus,
    val listedDate: String,
    val viewsCount: Int,
    val interestedCount: Int
)

enum class ListingStatus(val label: String) {
    ACTIVE("Active"),
    PENDING("Pending"),
    SOLD("Sold")
}

// ─── Filter option ──────────────────────────────────────────────────
enum class CollectionFilter(val label: String) {
    ALL("All"),
    ACTIVE("Active"),
    PENDING("Pending"),
    SOLD("Sold")
}

// ─── UI State ───────────────────────────────────────────────────────
data class CollectionUiState(
    val selectedFilter: CollectionFilter = CollectionFilter.ALL,
    val items: List<CollectionListingItem> = initDefaultListings(),
    val searchQuery: String = ""
) {
    val filteredItems: List<CollectionListingItem>
        get() {
            val filtered = when (selectedFilter) {
                CollectionFilter.ALL -> items
                CollectionFilter.ACTIVE -> items.filter { it.status == ListingStatus.ACTIVE }
                CollectionFilter.PENDING -> items.filter { it.status == ListingStatus.PENDING }
                CollectionFilter.SOLD -> items.filter { it.status == ListingStatus.SOLD }
            }
            return if (searchQuery.isBlank()) filtered
            else filtered.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.category.contains(searchQuery, ignoreCase = true)
            }
        }
}

class CollectionViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow(CollectionUiState())
    val uiState: StateFlow<CollectionUiState> = _uiState.asStateFlow()

    fun onFilterSelected(filter: CollectionFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}


// ─── Mock data ──────────────────────────────────────────────────────
private fun initDefaultListings(): List<CollectionListingItem> {
    return listOf(
        CollectionListingItem(
            id = "1",
            name = "Charizard Holo",
            category = "Trading Cards",
            estimatedValue = "$285.00",
            iconChar = "🔥",
            status = ListingStatus.ACTIVE,
            listedDate = "May 10",
            viewsCount = 47,
            interestedCount = 5
        ),
        CollectionListingItem(
            id = "2",
            name = "Game Boy Color",
            category = "Retro Games",
            estimatedValue = "$120.00",
            iconChar = "🎮",
            status = ListingStatus.ACTIVE,
            listedDate = "May 8",
            viewsCount = 32,
            interestedCount = 3
        ),
        CollectionListingItem(
            id = "3",
            name = "Levi's 501 '89",
            category = "Vintage Clothing",
            estimatedValue = "$95.00",
            iconChar = "👖",
            status = ListingStatus.PENDING,
            listedDate = "May 6",
            viewsCount = 28,
            interestedCount = 2
        ),
        CollectionListingItem(
            id = "4",
            name = "N64 Controller",
            category = "Retro Games",
            estimatedValue = "$45.00",
            iconChar = "🕹️",
            status = ListingStatus.ACTIVE,
            listedDate = "May 5",
            viewsCount = 19,
            interestedCount = 1
        ),
        CollectionListingItem(
            id = "5",
            name = "Pikachu 1st Ed.",
            category = "Trading Cards",
            estimatedValue = "$210.00",
            iconChar = "⚡",
            status = ListingStatus.SOLD,
            listedDate = "Apr 28",
            viewsCount = 63,
            interestedCount = 8
        ),
        CollectionListingItem(
            id = "6",
            name = "SNES Console",
            category = "Retro Games",
            estimatedValue = "$175.00",
            iconChar = "🎮",
            status = ListingStatus.ACTIVE,
            listedDate = "May 12",
            viewsCount = 15,
            interestedCount = 2
        ),
        CollectionListingItem(
            id = "7",
            name = "Vintage Nike Windbreaker",
            category = "Vintage Clothing",
            estimatedValue = "$88.00",
            iconChar = "🧥",
            status = ListingStatus.PENDING,
            listedDate = "May 3",
            viewsCount = 41,
            interestedCount = 4
        )
    )
}
