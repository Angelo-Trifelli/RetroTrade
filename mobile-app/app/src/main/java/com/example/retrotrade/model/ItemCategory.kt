package com.example.retrotrade.model

enum class ItemCategory(val label: String) {
    TRADING_CARDS("Trading Cards"),
    RETRO_GAMES("Retro Games"),
    VINTAGE_CLOTHING("Vintage Clothing"),
    VINYL_RECORDS("Vinyl Records"),
    COMICS("Comics"),
    TOYS("Toys & Figures"),
    ELECTRONICS("Electronics"),
    OTHER("Other");

    override fun toString(): String {
        return label
    }
}