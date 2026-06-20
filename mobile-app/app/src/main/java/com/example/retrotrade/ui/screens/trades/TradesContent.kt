package com.example.retrotrade.ui.screens.trades

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SwapHorizontalCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrotrade.model.Trade
import com.example.retrotrade.model.TradeStatus
import com.example.retrotrade.ui.components.common.AppHeader
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.trades.TradesUiState
import com.example.retrotrade.ui.theme.BackgroundColor
import com.example.retrotrade.ui.theme.RetroIcon
import com.example.retrotrade.ui.theme.RetroOrange
import com.example.retrotrade.ui.theme.RetroTextPrimary
import com.example.retrotrade.ui.theme.RetroTextSecondary


private val tabTitles = listOf("Active Offers", "Completed", "Rejected")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun TradesContent(
    modifier: Modifier = Modifier,
    uiState: TradesUiState = TradesUiState(),
    dataState: GenericUiState = GenericUiState.Idle,
    onTabSelected: (Int) -> Unit = {},
    onOpenChat: (Trade) -> Unit = {},
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
            // ── Header ──────────────────────────────────────────
            AppHeader(
                title = "Trade Center",
                subtitle = "Manage your trades",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Tab selector ────────────────────────────────────
            TradesTabRow(
                selectedIndex = uiState.selectedTabIndex,
                onTabSelected = onTabSelected
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Trades list ──────────────────────────────────────
            if (uiState.filteredTrades.isEmpty()) {
                EmptyState()
            } else {
                TradeList(
                    trades = uiState.filteredTrades,
                    onOpenChat = onOpenChat
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


// ─── Tab Row ────────────────────────────────────────────────────────
@Composable
private fun TradesTabRow(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(BackgroundColor, RoundedCornerShape(14.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabTitles.forEachIndexed { index, title ->
            val isSelected = selectedIndex == index
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else Color.Transparent,
                label = "tabBg"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) RetroOrange else RetroTextSecondary,
                label = "tabText"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onTabSelected(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp,
                    color = textColor
                )
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
            imageVector = Icons.Default.SwapHorizontalCircle,
            contentDescription = null,
            tint = RetroIcon,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No trades found",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = RetroTextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}


// ─── Active Trades Tab ──────────────────────────────────────────────
@Composable
private fun TradeList(
    trades: List<Trade>,
    onOpenChat: (Trade) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        trades.forEach { trade ->
            TradeCard(
                trade = trade,
                onOpenChat = onOpenChat
            )
        }
    }
}

@Composable
private fun TradeCard(
    trade: Trade,
    onOpenChat: (Trade) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable{ onOpenChat(trade) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ── Status + time ────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = trade.status)
                Text(
                    text = trade.timeAgo,
                    fontSize = 12.sp,
                    color = RetroTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Item and Offer details ───────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = trade.itemIcon, fontSize = 26.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (trade.isBuying) Icons.Default.ShoppingCart else Icons.Default.Person,
                            contentDescription = null,
                            tint = if (trade.isBuying) Color(0xFF2980B9) else Color(0xFF27AE60),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = if (trade.isBuying) "Buying from ${trade.sellerName}" else "Selling to ${trade.buyerName}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (trade.isBuying) Color(0xFF2980B9) else Color(0xFF27AE60)
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = trade.itemName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = RetroTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Offer: ${trade.offeredPrice}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = RetroOrange
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Asking: ${trade.requestedPrice}",
                            fontSize = 11.sp,
                            color = RetroTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Last message preview ─────────────────────────
            Text(
                text = "\"${trade.lastMessage}\"",
                fontSize = 13.sp,
                color = RetroTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundColor, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun StatusBadge(status: TradeStatus) {
    val (bgColor, textColor) = when (status) {
        TradeStatus.PENDING -> Color(0xFFFFF3E0) to Color(0xFFE65100)
        TradeStatus.ACCEPTED -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
        TradeStatus.COMPLETED -> Color(0xFFF3E5F5) to Color(0xFF2E7D32)
        TradeStatus.REJECTED -> Color(0xFFFFEBEE) to Color(0xFFC62828)
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
