package com.example.retrotrade.ui.screens.homepage

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.retrotrade.ui.components.common.AppBottomBar
import com.example.retrotrade.ui.components.common.AppErrorDialog
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.homepage.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dataState by viewModel.dataState.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { index, label ->
                    viewModel.onTabSelected(index, label)
                }
            )
        }
    ) { innerPadding ->
        if (dataState is GenericUiState.Error) {
            AppErrorDialog(
                title = "Error",
                message = (dataState as GenericUiState.Error).message,
                onDismiss = { viewModel.resetDataState() }
            )
        }

        HomeContent(
            uiState = uiState,
            dataState = dataState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
