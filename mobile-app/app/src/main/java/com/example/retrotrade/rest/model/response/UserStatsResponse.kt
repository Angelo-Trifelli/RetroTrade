package com.example.retrotrade.rest.model.response

data class UserStatsResponse(
    val activeItems: Int,
    val pendingTrades: Int,
    val soldItems: Int,
    val completedTrades: Int
)