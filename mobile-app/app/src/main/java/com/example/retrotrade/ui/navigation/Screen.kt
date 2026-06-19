package com.example.retrotrade.ui.navigation

sealed class Screen(val route: String) {

    // Graphs
    data object AuthGraph : Screen("auth_graph")
    data object MainGraph : Screen("main_graph")

    // Auth screens
    data object Login : Screen("login")
    data object Register : Screen("register")

    // Home tabs
    data object Home : Screen("home")
    data object Collection : Screen("collection")
    data object Scan : Screen("scan")
    data object Trades : Screen("trades")
    data object Profile : Screen("profile")

    data object Map : Screen("map")

    // Item Details screen
    data class ItemDetails(val subRoute: String = "item_details/{itemId}?source={source}") : Screen(subRoute) {
        companion object {
            fun createRoute(itemId: String, source: String = "collection") = "item_details/$itemId?source=$source"
        }
    }
}