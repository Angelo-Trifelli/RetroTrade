package com.example.retrotrade.ui.screens.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.retrotrade.ui.components.common.AppErrorDialog
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.item.ItemDetailsViewModel

@Composable
fun ItemDetailsScreen(
    viewModel: ItemDetailsViewModel = viewModel()
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

    ItemDetailsContent(
        uiState = uiState,
        dataState = dataState,
        onGoBack = { viewModel.onGoBack() },
        onSubmitOffer = { amount, message -> viewModel.onSubmitOffer(amount, message) }
    )
}
