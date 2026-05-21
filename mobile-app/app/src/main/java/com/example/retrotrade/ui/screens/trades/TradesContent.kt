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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrotrade.ui.navigation.trades.ActiveTrade
import com.example.retrotrade.ui.navigation.trades.TradeStatus
import com.example.retrotrade.ui.navigation.trades.TradeSuggestion
import com.example.retrotrade.ui.navigation.trades.TradesUiState
import com.example.retrotrade.ui.theme.BackgroundColor
import com.example.retrotrade.ui.theme.RetroIcon
import com.example.retrotrade.ui.theme.RetroOrange
import com.example.retrotrade.ui.theme.RetroTextPrimary
import com.example.retrotrade.ui.theme.RetroTextSecondary


private val tabTitles = listOf("Suggestions", "Active Offers")

@Composable
@Preview(showBackground = true)
fun TradesContent(
    modifier: Modifier = Modifier,
    uiState: TradesUiState = TradesUiState(),
    onTabSelected: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header ──────────────────────────────────────────
        TradesHeader()

        Spacer(modifier = Modifier.height(20.dp))

        // ── Tab selector ────────────────────────────────────
        TradesTabRow(
            selectedIndex = uiState.selectedTabIndex,
            onTabSelected = onTabSelected
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ── Content based on selected tab ───────────────────
        when (uiState.selectedTabIndex) {
            0 -> SuggestionsTab(suggestions = uiState.suggestions)
            1 -> ActiveTradesTab(trades = uiState.activeTrades)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


// ─── Header ─────────────────────────────────────────────────────────
@Composable
private fun TradesHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Trade Center",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = RetroTextPrimary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Find the perfect trade match",
            style = MaterialTheme.typography.bodyMedium,
            color = RetroTextSecondary
        )
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


// ─── Suggestions Tab ────────────────────────────────────────────────
@Composable
private fun SuggestionsTab(suggestions: List<TradeSuggestion>) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        suggestions.forEach { suggestion ->
            SuggestionCard(suggestion)
        }
    }
}

@Composable
private fun SuggestionCard(suggestion: TradeSuggestion) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // ── Match badge + seller info ────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Match percentage badge
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                listOf(Color(0xFF27AE60), Color(0xFF2ECC71))
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${suggestion.matchPercentage}% Match",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Seller info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person, null,
                        tint = RetroIcon,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = suggestion.sellerName,
                        fontSize = 12.sp,
                        color = RetroTextSecondary
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Default.LocationOn, null,
                        tint = RetroIcon,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = suggestion.sellerDistance,
                        fontSize = 12.sp,
                        color = RetroTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Item details ───────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(BackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = suggestion.itemIcon, fontSize = 32.sp)
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = suggestion.itemName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = RetroTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = suggestion.itemCategory,
                        fontSize = 12.sp,
                        color = RetroTextSecondary
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = suggestion.requestedPrice,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = RetroOrange
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = suggestion.marketValue,
                            fontSize = 12.sp,
                            color = RetroTextSecondary,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* Make Offer */ },
                    colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Make Offer", color = RetroOrange, fontWeight = FontWeight.Bold)
                }
                
                Button(
                    onClick = { /* Buy Now */ },
                    colors = ButtonDefaults.buttonColors(containerColor = RetroOrange),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Buy Now", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


// ─── Active Trades Tab ──────────────────────────────────────────────
@Composable
private fun ActiveTradesTab(trades: List<ActiveTrade>) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        trades.forEach { trade ->
            ActiveTradeCard(trade)
        }
    }
}

@Composable
private fun ActiveTradeCard(trade: ActiveTrade) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
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
                            text = if (trade.isBuying) "Buying from ${trade.partnerName}" else "Selling to ${trade.partnerName}",
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
        TradeStatus.ACCEPTED -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        TradeStatus.COUNTER_OFFER -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
        TradeStatus.COMPLETED -> Color(0xFFF3E5F5) to Color(0xFF7B1FA2)
        TradeStatus.DECLINED -> Color(0xFFFFEBEE) to Color(0xFFC62828)
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
