package com.example.retrotrade.rest.model.response

import com.example.retrotrade.model.ItemCategory
import com.example.retrotrade.model.ItemStatus

data class LoadItemsResponse(
    val id: String,
    val name: String,
    val category: ItemCategory,
    val estimatedValue: String,
    val iconChar: String,
    val status: ItemStatus,
    val latitude: Double,
    val longitude: Double
)