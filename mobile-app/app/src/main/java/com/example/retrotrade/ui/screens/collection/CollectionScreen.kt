package com.example.retrotrade.ui.screens.collection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.retrotrade.ui.navigation.collection.CollectionViewModel

@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CollectionContent(
        uiState = uiState,
        onFilterSelected = { viewModel.onFilterSelected(it) },
        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) }
    )
}
