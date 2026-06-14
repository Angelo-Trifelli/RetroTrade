package com.example.retrotrade.ui.screens.homepage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.retrotrade.ui.components.common.AppErrorDialog
import com.example.retrotrade.ui.components.common.SectionHeader
import com.example.retrotrade.ui.components.homepage.GreetingHeader
import com.example.retrotrade.ui.components.homepage.QuickActionsRow
import com.example.retrotrade.ui.components.homepage.RecentItemsRow
import com.example.retrotrade.ui.components.homepage.StatsStrip
import com.example.retrotrade.ui.components.homepage.TrendingItemsList
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.homepage.HomeUiState

@Composable
@Preview(showBackground = true)
fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState = HomeUiState(),
    dataState: GenericUiState = GenericUiState.Idle
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ── Greeting header ─────────────────────────────────
        GreetingHeader(
            username = uiState.username
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ── Quick-action cards ──────────────────────────────
        QuickActionsRow()

        Spacer(modifier = Modifier.height(24.dp))

        // ── Stats strip ─────────────────────────────────────
        StatsStrip(
            collectionCount = uiState.collectionCount,
            pendingTrades = uiState.pendingTradesCount
        )

        Spacer(modifier = Modifier.height(28.dp))

        // ── Recent items ────────────────────────────────────
        SectionHeader(title = "Recent Items")

        Spacer(modifier = Modifier.height(12.dp))

        RecentItemsRow(items = uiState.recentItems)

        Spacer(modifier = Modifier.height(28.dp))

        // ── Trending near you ───────────────────────────────
        SectionHeader(title = "Trending Near You")

        Spacer(modifier = Modifier.height(12.dp))

        TrendingItemsList(items = uiState.trendingItems)

        Spacer(modifier = Modifier.height(16.dp))
    }
}