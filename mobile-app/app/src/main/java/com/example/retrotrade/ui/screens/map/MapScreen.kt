package com.example.retrotrade.ui.screens.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.retrotrade.ui.components.common.AppErrorDialog
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.map.MapViewModel

@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val dataState by viewModel.dataState.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()

    if (dataState is GenericUiState.Error) {
        AppErrorDialog(
            title = "Error",
            message = (dataState as GenericUiState.Error).message,
            onDismiss = { viewModel.resetDataState() }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.startLocationTracking()
    }

    MapContent(
        uiState = uiState,
        dataState = dataState,
        userLocation = userLocation,
        onBack = { viewModel.onBack() },
        onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
        onItemCategoryChange = { viewModel.onItemCategoryChange(it) },
        onRadiusChange = { viewModel.onRadiusChange(it) },
        onFilterWindowClosed = { viewModel.onFilterWindowClosed() },
        onClusterClick       = { viewModel.onClusterClick(it) },
        onClusterDismiss     = { viewModel.onClusterDismiss() }
    )
}