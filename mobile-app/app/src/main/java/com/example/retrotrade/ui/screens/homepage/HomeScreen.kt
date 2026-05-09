package com.example.retrotrade.ui.screens.homepage

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.retrotrade.ui.navigation.homepage.CollectionItem
import com.example.retrotrade.ui.navigation.homepage.HomeViewModel
import com.example.retrotrade.ui.navigation.homepage.TrendingItem
import com.example.retrotrade.ui.theme.RetroIcon
import com.example.retrotrade.ui.theme.RetroOrange
import com.example.retrotrade.ui.theme.RetroTextPrimary
import com.example.retrotrade.ui.theme.RetroTextSecondary

// ─── Bottom nav tab data ────────────────────────────────────────────
private data class BottomNavTab(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val bottomNavTabs = listOf(
    BottomNavTab("Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavTab("Collection", Icons.Filled.GridView, Icons.Outlined.GridView),
    BottomNavTab("Scan", Icons.Filled.CameraAlt, Icons.Outlined.CameraAlt),
    BottomNavTab("Trades", Icons.Filled.SwapHoriz, Icons.Outlined.SwapHoriz),
    BottomNavTab("Profile", Icons.Filled.Person, Icons.Outlined.Person)
)

// ─── Root Composable ────────────────────────────────────────────────
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            RetroBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Greeting header ─────────────────────────────────
            GreetingHeader(username = uiState.username)

            Spacer(modifier = Modifier.height(20.dp))

            // ── Quick-action cards ──────────────────────────────
            QuickActionsRow(
                onScanClicked = { viewModel.onScanClicked() },
                onCollectionClicked = { viewModel.onCollectionClicked() },
                onTradesClicked = { viewModel.onTradesClicked() },
                onChatClicked = { viewModel.onChatClicked() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Stats strip ─────────────────────────────────────
            StatsStrip(
                collectionCount = uiState.collectionCount,
                pendingTrades = uiState.pendingTradesCount,
                totalValue = uiState.totalEstimatedValue
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
}

// ─── Greeting Header ────────────────────────────────────────────────
@Composable
private fun GreetingHeader(username: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hey, $username \uD83D\uDC4B",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = RetroTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Ready to discover something?",
                style = MaterialTheme.typography.bodyMedium,
                color = RetroTextSecondary
            )
        }
        IconButton(onClick = { /* notifications */ }) {
            Box {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = RetroIcon,
                    modifier = Modifier.size(28.dp)
                )
                // small orange badge dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 2.dp, y = (-1).dp)
                        .background(RetroOrange, CircleShape)
                )
            }
        }
    }
}

// ─── Quick-action cards ─────────────────────────────────────────────
@Composable
private fun QuickActionsRow(
    onScanClicked: () -> Unit,
    onCollectionClicked: () -> Unit,
    onTradesClicked: () -> Unit,
    onChatClicked: () -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            QuickActionCard(
                emoji = "📷", label = "Scan Item",
                gradientColors = listOf(Color(0xFFD35400), Color(0xFFE67E22)),
                onClick = onScanClicked
            )
        }
        item {
            QuickActionCard(
                emoji = "📦", label = "My Collection",
                gradientColors = listOf(Color(0xFF2980B9), Color(0xFF3498DB)),
                onClick = onCollectionClicked
            )
        }
        item {
            QuickActionCard(
                emoji = "🔄", label = "Find Trades",
                gradientColors = listOf(Color(0xFF27AE60), Color(0xFF2ECC71)),
                onClick = onTradesClicked
            )
        }
        item {
            QuickActionCard(
                emoji = "💬", label = "Chat",
                gradientColors = listOf(Color(0xFF8E44AD), Color(0xFF9B59B6)),
                onClick = onChatClicked
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    emoji: String,
    label: String,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(110.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradientColors)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = emoji, fontSize = 32.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = label,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

// ─── Stats strip ────────────────────────────────────────────────────
@Composable
private fun StatsStrip(
    collectionCount: Int,
    pendingTrades: Int,
    totalValue: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            value = collectionCount.toString(),
            label = "Items",
            accentColor = Color(0xFF2980B9),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = pendingTrades.toString(),
            label = "Pending",
            accentColor = RetroOrange,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = totalValue,
            label = "Value",
            accentColor = Color(0xFF27AE60),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    value: String,
    label: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = RetroTextSecondary
            )
        }
    }
}

// ─── Section header ─────────────────────────────────────────────────
@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = RetroTextPrimary,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
}

// ─── Recent items row ───────────────────────────────────────────────
@Composable
private fun RecentItemsRow(items: List<CollectionItem>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(items) { item ->
            RecentItemCard(item)
        }
    }
}

@Composable
private fun RecentItemCard(item: CollectionItem) {
    Card(
        modifier = Modifier
            .width(155.dp)
            .height(175.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Emoji icon area
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF3EBE1)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.iconChar, fontSize = 26.sp)
            }

            Column {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = RetroTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.category,
                    fontSize = 11.sp,
                    color = RetroTextSecondary
                )
            }

            Text(
                text = item.estimatedValue,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = RetroOrange
            )
        }
    }
}

// ─── Trending items list ────────────────────────────────────────────
@Composable
private fun TrendingItemsList(items: List<TrendingItem>) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items.forEach { item ->
            TrendingItemCard(item)
        }
    }
}

@Composable
private fun TrendingItemCard(item: TrendingItem) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF3EBE1)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.iconChar, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = RetroTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.category,
                    fontSize = 12.sp,
                    color = RetroTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = RetroIcon,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = item.ownerName,
                        fontSize = 11.sp,
                        color = RetroTextSecondary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = RetroIcon,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = item.distance,
                        fontSize = 11.sp,
                        color = RetroTextSecondary
                    )
                }
            }

            // Price tag
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.estimatedValue,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = RetroOrange
                )
            }
        }
    }
}

// ─── Bottom navigation bar ──────────────────────────────────────────
@Composable
private fun RetroBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        bottomNavTabs.forEachIndexed { index, tab ->
            val isSelected = selectedTab == index
            val isScanTab = index == 2

            val animatedScale by animateFloatAsState(
                targetValue = if (isSelected) 1.15f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                label = "scale"
            )
            val animatedColor by animateColorAsState(
                targetValue = if (isSelected) RetroOrange else RetroIcon,
                label = "color"
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    if (isScanTab) {
                        // Prominent center FAB-style button
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .scale(animatedScale)
                                .shadow(
                                    elevation = if (isSelected) 8.dp else 4.dp,
                                    shape = CircleShape
                                )
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(Color(0xFFD35400), Color(0xFFE67E22))
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.label,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.label,
                            tint = animatedColor,
                            modifier = Modifier
                                .size(24.dp)
                                .scale(animatedScale)
                        )
                    }
                },
                label = {
                    if (!isScanTab) {
                        Text(
                            text = tab.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = animatedColor
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
