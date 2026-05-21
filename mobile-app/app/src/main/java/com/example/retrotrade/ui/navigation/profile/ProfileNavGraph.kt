package com.example.retrotrade.ui.navigation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.retrotrade.ui.navigation.NavEvent
import com.example.retrotrade.ui.navigation.Screen
import com.example.retrotrade.ui.navigation.slideInFromLeft
import com.example.retrotrade.ui.navigation.slideInFromRight
import com.example.retrotrade.ui.navigation.slideOutToLeft
import com.example.retrotrade.ui.navigation.slideOutToRight
import com.example.retrotrade.ui.screens.profile.ProfileScreen


fun NavGraphBuilder.profileRoute(
    navController: NavController
) {
    composable(
        route = Screen.Profile.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        val profileViewModel: ProfileViewModel = viewModel()
        ObserveProfileNavigation(profileViewModel, navController)
        ProfileScreen(profileViewModel)
    }
}


@Composable
private fun ObserveProfileNavigation(
    viewModel: ProfileViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is NavEvent.Navigate -> {
                    navController.navigate(event.route) {
                        // On logout, clear the entire back stack
                        if (event.route == Screen.AuthGraph.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                NavEvent.PopBackStack ->
                    navController.popBackStack()
            }
        }
    }
}
