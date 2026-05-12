package com.example.retrotrade.ui.components.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class QuickActionSpec(
    val emoji: String,
    val label: String,
    val gradientColors: List<Color>,
    val onClick: () -> Unit,
)

@Composable
@Preview(showBackground = true)
fun QuickActionsRow(
    onScanClicked: () -> Unit = {},
    onCollectionClicked: () -> Unit = {},
    onTradesClicked: () -> Unit = {},
    onChatClicked: () -> Unit = {}
) {
    val actions = remember(onScanClicked, onCollectionClicked, onTradesClicked, onChatClicked) {
        listOf(
            QuickActionSpec("📷", "Scan Item", listOf(Color(0xFFD35400), Color(0xFFE67E22)), onScanClicked),
            QuickActionSpec("📦", "My Collection", listOf(Color(0xFF2980B9), Color(0xFF3498DB)), onCollectionClicked),
            QuickActionSpec("🔄", "Find Trades", listOf(Color(0xFF27AE60), Color(0xFF2ECC71)), onTradesClicked),
            QuickActionSpec("💬", "Chat", listOf(Color(0xFF8E44AD), Color(0xFF9B59B6)), onChatClicked),
        )
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(actions, key = { it.label }) { spec ->
            QuickActionCard(
                emoji = spec.emoji,
                label = spec.label,
                gradientColors = spec.gradientColors,
                onClick = spec.onClick,
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
private fun QuickActionCard(
    emoji: String = "",
    label: String = "",
    gradientColors: List<Color> = emptyList(),
    onClick: () -> Unit = {}
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