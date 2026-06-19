package com.example.retrotrade.model

enum class TradeStatus(val label: String) {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    COMPLETED("Completed"),
    REJECTED("Rejected")
}