package com.example.retrotrade.ui.screens.collection

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.example.retrotrade.ui.navigation.collection.CollectionFilter
import com.example.retrotrade.ui.navigation.collection.CollectionListingItem
import com.example.retrotrade.ui.navigation.collection.CollectionUiState
import com.example.retrotrade.ui.navigation.collection.ListingStatus
import com.example.retrotrade.ui.theme.BackgroundColor
import com.example.retrotrade.ui.theme.RetroIcon
import com.example.retrotrade.ui.theme.RetroOrange
import com.example.retrotrade.ui.theme.RetroTextPrimary
import com.example.retrotrade.ui.theme.RetroTextSecondary


@Composable
@Preview(showBackground = true)
fun CollectionContent(
    modifier: Modifier = Modifier,
    uiState: CollectionUiState = CollectionUiState(),
    onFilterSelected: (CollectionFilter) -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header ──────────────────────────────────────────
        CollectionHeader(itemCount = uiState.items.size)

        Spacer(modifier = Modifier.height(16.dp))

        // ── Search bar ──────────────────────────────────────
        SearchBar(
            query = uiState.searchQuery,
            onQueryChanged = onSearchQueryChanged
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            ItemsList(items = uiState.filteredItems)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


// ─── Header ─────────────────────────────────────────────────────────
@Composable
private fun CollectionHeader(itemCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "My Collection",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = RetroTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$itemCount items listed",
                style = MaterialTheme.typography.bodyMedium,
                color = RetroTextSecondary
            )
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(BackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = null,
                tint = RetroOrange,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


// ─── Search bar ─────────────────────────────────────────────────────
@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text("Search your items...", color = RetroIcon)
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = RetroIcon
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            focusedBorderColor = RetroOrange,
            unfocusedBorderColor = BackgroundColor,
            cursorColor = RetroOrange
        )
    )
}


// ─── Filter chips ───────────────────────────────────────────────────
@Composable
private fun FilterChipsRow(
    selectedFilter: CollectionFilter,
    onFilterSelected: (CollectionFilter) -> Unit,
    items: List<CollectionListingItem>
) {
    LazyRow(
        modifier = Modifier.padding(start = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(CollectionFilter.entries) { filter ->
            val isSelected = selectedFilter == filter
            val count = when (filter) {
                CollectionFilter.ALL -> items.size
                CollectionFilter.ACTIVE -> items.count { it.status == ListingStatus.ACTIVE }
                CollectionFilter.PENDING -> items.count { it.status == ListingStatus.PENDING }
                CollectionFilter.SOLD -> items.count { it.status == ListingStatus.SOLD }
            }

            val bgColor by animateColorAsState(
                targetValue = if (isSelected) RetroOrange else Color.White,
                label = "chipBg"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else RetroTextSecondary,
                label = "chipText"
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onFilterSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "${filter.label} ($count)",
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = textColor
                )
            }
        }
    }
}


// ─── Items list ─────────────────────────────────────────────────────
@Composable
private fun ItemsList(items: List<CollectionListingItem>) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items.forEach { item ->
            CollectionItemCard(item)
        }
    }
}

@Composable
private fun CollectionItemCard(item: CollectionListingItem) {
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
            // Emoji icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(BackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.iconChar, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
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
                    }
                    StatusBadge(status = item.status)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom row: price + stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.estimatedValue,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = RetroOrange
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.RemoveRedEye, null,
                            tint = RetroIcon,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(3.dp))
                        Text(
                            text = "${item.viewsCount}",
                            fontSize = 12.sp,
                            color = RetroTextSecondary
                        )
                        Spacer(Modifier.width(12.dp))
                        Icon(
                            Icons.Default.ThumbUp, null,
                            tint = RetroIcon,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(3.dp))
                        Text(
                            text = "${item.interestedCount}",
                            fontSize = 12.sp,
                            color = RetroTextSecondary
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = item.listedDate,
                            fontSize = 12.sp,
                            color = RetroTextSecondary
                        )
                    }
                }
            }
        }
    }
}


// ─── Status badge ───────────────────────────────────────────────────
@Composable
private fun StatusBadge(status: ListingStatus) {
    val (bgColor, textColor) = when (status) {
        ListingStatus.ACTIVE -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        ListingStatus.PENDING -> Color(0xFFFFF3E0) to Color(0xFFE65100)
        ListingStatus.SOLD -> Color(0xFFF3E5F5) to Color(0xFF7B1FA2)
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
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
        Text(
            text = "Try adjusting your filters",
            style = MaterialTheme.typography.bodyMedium,
            color = RetroTextSecondary
        )
    }
}
