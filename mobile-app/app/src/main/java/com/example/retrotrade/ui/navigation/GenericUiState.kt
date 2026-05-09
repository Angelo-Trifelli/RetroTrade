package com.example.retrotrade.ui.navigation

sealed class GenericUiState {
    object Idle : GenericUiState()
    object Loading : GenericUiState()
    object Success : GenericUiState()
    data class Error(val message: String) : GenericUiState()
}