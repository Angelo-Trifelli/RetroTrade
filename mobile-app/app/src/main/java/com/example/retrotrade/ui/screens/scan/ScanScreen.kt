package com.example.retrotrade.ui.screens.scan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.retrotrade.ui.components.common.AppErrorDialog
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.scan.ScanViewModel

@Composable
fun ScanScreen(
    viewModel: ScanViewModel = viewModel()
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

    ScanContent(
        uiState = uiState,
        dataState = dataState,
        onPhotoCaptured = { viewModel.onPhotoCaptured(it) },
        onClearPhoto = { viewModel.onClearPhoto() },
        onItemNameChanged = { viewModel.onItemNameChanged(it) },
        onCategorySelected = { viewModel.onCategorySelected(it) },
        onEstimatedValueChanged = { viewModel.onEstimatedValueChanged(it) },
        onIconCharSelected = { viewModel.onIconCharSelected(it) },
        onSubmit = { viewModel.onSubmit() },
        onSuccessDismissed = { viewModel.onSuccessDismissed() }
    )
}
