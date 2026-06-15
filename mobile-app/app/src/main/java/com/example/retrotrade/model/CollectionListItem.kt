package com.example.retrotrade.model

data class CollectionListItem(
    val id: String = "",
    val name: String = "",
    val category: ItemCategory = ItemCategory.OTHER,
    val estimatedValue: String = "",
    val iconChar: String = "",
    val status: ItemStatus = ItemStatus.ACTIVE
)