package com.example.retrotrade.model

data class CollectionListItem(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val estimatedValue: String = "",
    val iconChar: String = "",
    val status: ItemStatus = ItemStatus.ACTIVE
)