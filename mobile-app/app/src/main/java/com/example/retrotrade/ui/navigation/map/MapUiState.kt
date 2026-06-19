package com.example.retrotrade.ui.navigation.map

import com.example.retrotrade.model.ItemCategory
import com.example.retrotrade.rest.model.response.LoadItemsResponse

data class MapUiState(
    val mapLoaded: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: ItemCategory = ItemCategory.ALL,
    val radiusKm: Float = 10f,
    val items: List<LoadItemsResponse> = emptyList(),
    val selectedClusterItems: List<LoadItemsResponse>  = emptyList()
)