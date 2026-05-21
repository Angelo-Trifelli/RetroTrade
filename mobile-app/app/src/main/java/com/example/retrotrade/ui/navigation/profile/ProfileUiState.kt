package com.example.retrotrade.ui.navigation.profile

data class ProfileUiState(
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val memberSince: String = "April 2026",
    val soldItems: Int = 24,
    val completedTrades: Int = 7
)