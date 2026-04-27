package com.example.retrotrade.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.retrotrade.ui.navigation.authentication.authNavGraph

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screen.AuthGraph.route) {
        authNavGraph(navController)
    }
}