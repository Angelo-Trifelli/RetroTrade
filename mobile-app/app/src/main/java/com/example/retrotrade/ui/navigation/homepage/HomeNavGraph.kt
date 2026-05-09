package com.example.retrotrade.ui.navigation.homepage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.retrotrade.ui.navigation.NavEvent
import com.example.retrotrade.ui.navigation.Screen
import com.example.retrotrade.ui.navigation.slideInFromLeft
import com.example.retrotrade.ui.navigation.slideInFromRight
import com.example.retrotrade.ui.navigation.slideOutToLeft
import com.example.retrotrade.ui.navigation.slideOutToRight
import com.example.retrotrade.ui.screens.homepage.HomeScreen


fun NavGraphBuilder.homeNavGraph(
    navController: NavController
) {
    navigation(
        route = Screen.HomeGraph.route,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            val homeViewModel: HomeViewModel = viewModel()
            ObserveHomeNavigation(homeViewModel, navController)
            HomeScreen(homeViewModel)
        }
    }
}


@Composable
private fun ObserveHomeNavigation(
    viewModel: HomeViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is NavEvent.Navigate ->
                    navController.navigate(event.route)

                NavEvent.PopBackStack ->
                    navController.popBackStack()
            }
        }
    }
}
