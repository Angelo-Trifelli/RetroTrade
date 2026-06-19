package com.example.retrotrade.rest.model.request

import com.example.retrotrade.model.ItemCategory

data class CreateItemRequest(
    val photo: String,
    val name: String,
    val category: ItemCategory,
    val estimatedValue: String,
    val iconChar: String,
    val latitude: Double,
    val longitude: Double
)