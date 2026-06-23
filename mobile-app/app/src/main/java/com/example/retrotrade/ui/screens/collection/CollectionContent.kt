package com.example.retrotrade.ui.screens.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrotrade.ui.components.collection.FilterChipsRow
import com.example.retrotrade.ui.components.collection.ItemList
import com.example.retrotrade.ui.components.common.AppHeader
import com.example.retrotrade.ui.components.common.SearchBar
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.collection.CollectionUiState
import com.example.retrotrade.ui.theme.RetroIcon
import com.example.retrotrade.ui.theme.RetroTextPrimary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun CollectionContent(
    modifier: Modifier = Modifier,
    uiState: CollectionUiState = CollectionUiState(),
    dataState: GenericUiState = GenericUiState.Idle,
    onRefresh: () -> Unit = {},
    onFilterSelected: (CollectionFilter) -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {},
    onItemSelected: (String) -> Unit = {}
) {

    val isRefreshing = dataState is GenericUiState.Loading

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Header ──────────────────────────────────────────
            AppHeader(
                title = "My Collection",
                subtitle = "${uiState.items.size} items listed",
                icon = Icons.Default.Inventory2
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Search bar ──────────────────────────────────────
            SearchBar(
                query = uiState.searchQuery,
                onQueryChanged = onSearchQueryChanged,
                placeholder = "Search your items..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (dataState is GenericUiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = Color.Black
                    )
                }
            } else {
                // ── Filter chips ────────────────────────────────────
                FilterChipsRow(
                    selectedFilter = uiState.selectedFilter,
                    onFilterSelected = onFilterSelected,
                    items = uiState.items
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Items list ──────────────────────────────────────
                if (uiState.filteredItems.isEmpty()) {
                    EmptyState()
                } else {
                    ItemList(
                        items = uiState.filteredItems,
                        onItemSelected = { onItemSelected(it.id) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

        }
    }
}





// ─── Empty state ────────────────────────────────────────────────────
@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Inventory2,
            contentDescription = null,
            tint = RetroIcon,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No items found",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = RetroTextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}
