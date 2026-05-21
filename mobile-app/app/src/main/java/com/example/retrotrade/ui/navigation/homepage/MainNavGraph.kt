package com.example.retrotrade.ui.navigation.homepage

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.retrotrade.ui.navigation.Screen
import com.example.retrotrade.ui.screens.main.MainScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavController
) {
    navigation(
        route = Screen.MainGraph.route,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }
    }
}