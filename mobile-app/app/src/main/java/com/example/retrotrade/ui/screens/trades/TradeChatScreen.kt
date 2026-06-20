package com.example.retrotrade.ui.screens.trades

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.retrotrade.ui.components.common.AppErrorDialog
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.trades.TradeChatViewModel

@Composable
fun TradeChatScreen(
    viewModel: TradeChatViewModel = viewModel()
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

    TradeChatContent(
        uiState = uiState,
        dataState = dataState,
        onBack = viewModel::onGoBack,
        onSendMessage = viewModel::sendMessage,
        onCompleteTrade = viewModel::completeTrade,
        onAcceptTrade = viewModel::acceptTrade,
        onRejectTrade = viewModel::rejectTrade
    )
}