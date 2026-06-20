package com.example.retrotrade.rest.model.request

data class CreateTradeRequest(
    val itemId: String,
    val amount: String,
    val message: String?
)