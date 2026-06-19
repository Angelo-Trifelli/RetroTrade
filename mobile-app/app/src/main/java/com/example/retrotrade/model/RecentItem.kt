package com.example.retrotrade.model

data class RecentItem(
    val name: String = "",
    val category: ItemCategory = ItemCategory.OTHER,
    val estimatedValue: String = "",
    val iconChar: String = ""
)