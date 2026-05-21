package com.example.retrotrade.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.retrotrade.ui.components.common.AppBottomBar
import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.NavEvent
import com.example.retrotrade.ui.navigation.Screen
import com.example.retrotrade.ui.navigation.collection.CollectionViewModel
import com.example.retrotrade.ui.navigation.homepage.HomeViewModel
import com.example.retrotrade.ui.navigation.profile.ProfileViewModel
import com.example.retrotrade.ui.navigation.scan.ScanViewModel
import com.example.retrotrade.ui.navigation.trades.TradesViewModel
import com.example.retrotrade.ui.screens.collection.CollectionScreen
import com.example.retrotrade.ui.screens.homepage.HomeScreen
import com.example.retrotrade.ui.screens.profile.ProfileScreen
import com.example.retrotrade.ui.screens.scan.ScanScreen
import com.example.retrotrade.ui.screens.trades.TradesScreen
import com.example.retrotrade.ui.theme.RetroIcon
import com.example.retrotrade.ui.theme.RetroTextPrimary
import com.example.retrotrade.ui.theme.RetroTextSecondary


@Composable
fun MainScreen(
    navController: NavController
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            AppBottomBar(
                navController = bottomNavController
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                val homeViewModel: HomeViewModel = viewModel()
                ObserveNavEvents(homeViewModel, navController)
                HomeScreen(homeViewModel)
            }

            composable(Screen.Collection.route) {
                val collectionViewModel: CollectionViewModel = viewModel()
                ObserveNavEvents(collectionViewModel, navController)
                CollectionScreen(collectionViewModel)
            }

            composable(Screen.Scan.route) {
                val scanViewModel: ScanViewModel = viewModel()
                ObserveNavEvents(scanViewModel, navController)
                ScanScreen(scanViewModel)
            }

            composable(Screen.Trades.route) {
                val tradesViewModel: TradesViewModel = viewModel()
                ObserveNavEvents(tradesViewModel, navController)
                TradesScreen(tradesViewModel)
            }

            composable(Screen.Profile.route) {
                val profileViewModel: ProfileViewModel = viewModel()
                ObserveNavEvents(profileViewModel, navController)
                ProfileScreen(profileViewModel)
            }
        }
    }
}

@Composable
private fun PlaceholderTab(
    tabName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Construction,
            contentDescription = null,
            tint = RetroIcon,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = tabName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = RetroTextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Coming soon",
            style = MaterialTheme.typography.bodyMedium,
            color = RetroTextSecondary
        )
    }
}

@Composable
private fun ObserveNavEvents(
    viewModel: BaseViewModel,
    navController: NavController
) {
    LaunchedEffect(viewModel) {
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
