package com.example.retrotrade.rest.model.response

import com.example.retrotrade.model.ItemCategory
import com.example.retrotrade.model.ItemStatus

data class ItemDetailsResponse(
    val id: String,
    val name: String,
    val category: ItemCategory,
    val estimatedValue: String,
    val iconChar: String,
    val status: ItemStatus,
    val photo: String? // Base64 string
)
