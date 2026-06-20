package com.example.retrotrade.ui.navigation.homepage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.NavEvent
import com.example.retrotrade.ui.navigation.Screen
import com.example.retrotrade.ui.navigation.item.ItemDetailsViewModel
import com.example.retrotrade.ui.navigation.map.MapViewModel
import com.example.retrotrade.ui.navigation.trades.TradeChatViewModel
import com.example.retrotrade.ui.screens.item.ItemDetailsScreen
import com.example.retrotrade.ui.screens.main.MainScreen
import com.example.retrotrade.ui.screens.map.MapScreen
import com.example.retrotrade.ui.screens.trades.TradeChatScreen

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

        composable(
            route = Screen.ItemDetails().route,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.StringType
                },
                navArgument("source") {
                    type = NavType.StringType
                    defaultValue = "collection"
                }
            )
        ) {
            val itemDetailsViewModel: ItemDetailsViewModel = viewModel()
            ObserveNavigation(itemDetailsViewModel, navController)
            ItemDetailsScreen(itemDetailsViewModel)
        }

        composable(route = Screen.Map.route) {
            val mapViewModel: MapViewModel = viewModel()
            ObserveNavigationMap(mapViewModel, navController)
            MapScreen(mapViewModel)
        }

        composable(
            route = Screen.TradeChat().route,
            arguments = listOf(
                navArgument("tradeId") {
                    type = NavType.StringType
                }
            )
        ) {
            val tradeChatViewModel: TradeChatViewModel = viewModel()
            ObserveNavigation(tradeChatViewModel, navController)
            TradeChatScreen(tradeChatViewModel)
        }
    }
}


@Composable
private fun ObserveNavigation(
    viewModel: BaseViewModel,
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

@Composable
private fun ObserveNavigationMap(
    viewModel: MapViewModel,
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