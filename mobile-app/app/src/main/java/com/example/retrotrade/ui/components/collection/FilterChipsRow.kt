package com.example.retrotrade.ui.components.collection

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrotrade.model.CollectionListItem
import com.example.retrotrade.model.ItemStatus
import com.example.retrotrade.ui.screens.collection.CollectionFilter
import com.example.retrotrade.ui.theme.RetroOrange
import com.example.retrotrade.ui.theme.RetroTextSecondary

@Composable
@Preview(showBackground = true)
fun FilterChipsRow(
    selectedFilter: CollectionFilter = CollectionFilter.ALL,
    onFilterSelected: (CollectionFilter) -> Unit = {},
    items: List<CollectionListItem> = emptyList()
) {

    LazyRow(
        modifier = Modifier.padding(start = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(CollectionFilter.entries) { filter ->
            val isSelected = selectedFilter == filter
            val count = when (filter) {
                CollectionFilter.ALL -> items.size
                CollectionFilter.ACTIVE -> items.count { it.status == ItemStatus.ACTIVE }
                CollectionFilter.PENDING -> items.count { it.status == ItemStatus.PENDING }
                CollectionFilter.SOLD -> items.count { it.status == ItemStatus.SOLD }
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