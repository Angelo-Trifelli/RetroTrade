package com.example.retrotrade.ui.navigation.map

import com.example.retrotrade.model.ItemCategory

data class MapUiState(
    val mapLoaded: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: ItemCategory = ItemCategory.ALL,
    val radiusKm: Float = 10f
)