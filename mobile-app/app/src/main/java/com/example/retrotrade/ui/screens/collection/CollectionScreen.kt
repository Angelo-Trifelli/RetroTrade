package com.example.retrotrade.ui.screens.collection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.retrotrade.ui.components.common.AppErrorDialog
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.collection.CollectionViewModel

@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dataState by viewModel.dataState.collectAsState()

    if (dataState is GenericUiState.Error) {
        AppErrorDialog(
            title = "Error",
            message = (dataState as GenericUiState.Error).message,
            onDismiss = { viewModel.resetDataState() }
        )
    }

    CollectionContent(
        uiState = uiState,
        dataState = dataState,
        onRefresh = { viewModel.onRefresh() },
        onFilterSelected = { viewModel.onFilterSelected(it) },
        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
        onItemSelected = { viewModel.onItemSelected(it) }
    )
}
