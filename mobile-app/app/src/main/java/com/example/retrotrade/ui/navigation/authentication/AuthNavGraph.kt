package com.example.retrotrade.ui.navigation.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.NavEvent
import com.example.retrotrade.ui.navigation.Screen
import com.example.retrotrade.ui.navigation.slideInFromLeft
import com.example.retrotrade.ui.navigation.slideInFromRight
import com.example.retrotrade.ui.navigation.slideOutToLeft
import com.example.retrotrade.ui.navigation.slideOutToRight
import com.example.retrotrade.ui.screens.authentication.LoginScreen
import com.example.retrotrade.ui.screens.authentication.RegisterScreen


fun NavGraphBuilder.authNavGraph(
    navController: NavController
) {

    navigation(
        route = Screen.AuthGraph.route,
        startDestination = Screen.Login.route
    ) {
        composable(
            route = Screen.Login.route,
            enterTransition = { slideInFromLeft() },
            exitTransition = { slideOutToLeft() }
        ) {
            val loginModel: LoginViewModel = viewModel()
            ObserveNavigation(loginModel, navController)
            LoginScreen(loginModel)
        }

        composable(
            route = Screen.Register.route,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToRight() }
        ) {
            val registerModel: RegisterViewModel = viewModel()
            ObserveNavigation(registerModel, navController)
            RegisterScreen(registerModel)
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