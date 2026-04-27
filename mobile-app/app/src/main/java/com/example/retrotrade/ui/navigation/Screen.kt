package com.example.retrotrade.ui.navigation

sealed class Screen(val route: String) {

    // Graphs
    data object AuthGraph : Screen("auth_graph")

    // Auth screens
    data object Login : Screen("login")
    data object Register : Screen("register")
}