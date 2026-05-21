package com.example.retrotrade.ui.screens.scan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.retrotrade.ui.navigation.scan.ScanViewModel

@Composable
fun ScanScreen(
    viewModel: ScanViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ScanContent(
        uiState = uiState,
        onPhotoCaptured = { viewModel.onPhotoCaptured(it) },
        onClearPhoto = { viewModel.onClearPhoto() },
        onItemNameChanged = { viewModel.onItemNameChanged(it) },
        onCategorySelected = { viewModel.onCategorySelected(it) },
        onCategoryDropdownToggle = { viewModel.onCategoryDropdownToggle() },
        onCategoryDropdownDismiss = { viewModel.onCategoryDropdownDismiss() },
        onEstimatedValueChanged = { viewModel.onEstimatedValueChanged(it) },
        onIconCharSelected = { viewModel.onIconCharSelected(it) },
        onSubmit = { viewModel.onSubmit() },
        onSuccessDismissed = { viewModel.onSuccessDismissed() }
    )
}
