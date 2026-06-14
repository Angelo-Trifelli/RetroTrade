package com.example.retrotrade.rest.model.response

import com.example.retrotrade.model.ItemStatus

data class LoadItemsResponse(
    val id: String,
    val name: String,
    val category: String,
    val estimatedValue: String,
    val iconChar: String,
    val status: ItemStatus
)