package com.example.retrotrade.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.retrotrade.ui.navigation.authentication.authNavGraph
import com.example.retrotrade.ui.navigation.homepage.homeNavGraph

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screen.AuthGraph.route) {
        authNavGraph(navController)
        homeNavGraph(navController)
    }
}