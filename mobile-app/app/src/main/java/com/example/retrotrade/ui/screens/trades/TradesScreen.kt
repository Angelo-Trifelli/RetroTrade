package com.example.retrotrade.ui.screens.trades

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.retrotrade.ui.navigation.trades.TradesViewModel

@Composable
fun TradesScreen(
    viewModel: TradesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    TradesContent(
        uiState = uiState,
        onTabSelected = { viewModel.onTabSelected(it) }
    )
}
