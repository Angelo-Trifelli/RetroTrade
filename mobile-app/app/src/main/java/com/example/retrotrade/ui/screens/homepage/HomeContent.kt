package com.example.retrotrade.ui.screens.homepage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.retrotrade.ui.components.common.SectionHeader
import com.example.retrotrade.ui.components.homepage.FindTradesBanner
import com.example.retrotrade.ui.components.homepage.GreetingHeader
import com.example.retrotrade.ui.components.homepage.RecentItemsRow
import com.example.retrotrade.ui.components.homepage.StatsStrip
import com.example.retrotrade.ui.components.homepage.TrendingItemsList
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.homepage.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState = HomeUiState(),
    dataState: GenericUiState = GenericUiState.Idle,
    onFindTrades: () -> Unit = {},
    onRefresh: () -> Unit = {}
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
            // ── Greeting header ─────────────────────────────────
            GreetingHeader(
                username = uiState.username
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Quick-action cards ──────────────────────────────
            FindTradesBanner(onFindTrades = onFindTrades)

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
        }
    }
}