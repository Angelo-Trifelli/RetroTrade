package com.example.retrotrade.model


data class Trade(
    val id: String,
    val sellerName: String,
    val buyerName: String,
    val itemName: String,
    val itemIcon: String,
    val isBuying: Boolean,
    val requestedPrice: String,
    val offeredPrice: String,
    val status: TradeStatus,
    val lastMessage: String,
    val timeAgo: String
)