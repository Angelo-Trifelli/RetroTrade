package com.example.retrotrade.ui.screens.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrotrade.model.ItemCategory
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.map.MapUiState
import com.google.maps.android.compose.GoogleMap

// ── Colour tokens ──────────────────────────────────────────────────────────────
private val Surface       = Color(0xFF1C1B1F)
private val SurfacePanel  = Color(0xE61C1B1F)   // 90 % opaque
private val Accent        = Color(0xFFD4820A)
private val AccentMuted   = Color(0x33D4820A)   // 20 % tint for selected chip bg
private val TextPrimary   = Color(0xFFF5F0E8)
private val TextSecondary = Color(0xFFB0A99F)
private val ChipBorder    = Color(0xFF3A3830)
private val SliderTrack   = Color(0xFF3A3830)

private val ALL_CATEGORIES = ItemCategory.entries.toList().filter { it != ItemCategory.OTHER }


@Composable
fun MapContent(
    uiState: MapUiState       = MapUiState(),
    dataState: GenericUiState = GenericUiState.Idle,
    onBack: () -> Unit        = {},
    onSearchQueryChange: (String) -> Unit = {},
    onItemCategoryChange: (ItemCategory) -> Unit = {},
    onRadiusChange: (Float) -> Unit = {},
    onFilterWindowClosed: () -> Unit
) {

    var filtersExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Map ───────
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            onMapLoaded = { /* handle map loaded */ }
        )

        // ── Top gradient scrim — keeps search bar legible ──────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Surface.copy(alpha = 0.72f), Color.Transparent)
                    )
                )
        )

        // ── Back button + Search bar row ───────────────────────────────────────────
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Back button
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(SurfacePanel)
                    .border(1.dp, ChipBorder, CircleShape)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back",
                    tint               = TextPrimary,
                    modifier           = Modifier.size(20.dp)
                )
            }

            // Search bar
            SearchBar(
                query          = uiState.searchQuery,
                onQueryChange  = onSearchQueryChange,
                filtersActive  = filtersExpanded,
                onToggleFilter = { filtersExpanded = !filtersExpanded },
                modifier       = Modifier.weight(1f)
            )
        }

        // ── Bottom filter panel ────────────────────────────
        AnimatedVisibility(
            visible = filtersExpanded,
            enter   = slideInVertically(initialOffsetY = { it }),
            exit    = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            FilterPanel(
                selectedCategory = uiState.selectedCategory.label,
                onCategorySelect = { label ->
                    onItemCategoryChange(ALL_CATEGORIES.first { it.label == label })
                },
                radiusKm         = uiState.radiusKm,
                onRadiusChange   = onRadiusChange,
                onClose          = {
                    filtersExpanded = false
                    onFilterWindowClosed()
                }
            )
        }

        // ── Persistent bottom pill ──────────
        if (!filtersExpanded) {
            ActiveFilterBadge(
                radiusKm       = uiState.radiusKm,
                categoryLabel  = ALL_CATEGORIES.first { it.label == uiState.selectedCategory.label }.label,
                onClick        = { filtersExpanded = true },
                modifier       = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp)
            )
        }
    }
}

// ── Search bar ─────────────────────────────────────────────────────────────────
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    filtersActive: Boolean,
    onToggleFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(SurfacePanel)
            .border(1.dp, ChipBorder, RoundedCornerShape(26.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector        = Icons.Default.Search,
            contentDescription = null,
            tint               = TextSecondary,
            modifier           = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        TextField(
            value         = query,
            onValueChange = onQueryChange,
            placeholder   = {
                Text("Search items…", color = TextSecondary, fontSize = 15.sp)
            },
            singleLine    = true,
            colors        = TextFieldDefaults.colors(
                focusedContainerColor   = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor        = TextPrimary,
                unfocusedTextColor      = TextPrimary,
                cursorColor             = Accent,
                focusedIndicatorColor   = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier      = Modifier.weight(1f)
        )
        if (query.isNotEmpty()) {
            IconButton(onClick = { onQueryChange("") }, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextSecondary)
            }
        }
        Spacer(Modifier.width(4.dp))
        // Filter toggle button
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(if (filtersActive) AccentMuted else Color.Transparent)
                .clickable { onToggleFilter() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = Icons.Default.Tune,
                contentDescription = "Filters",
                tint               = if (filtersActive) Accent else TextSecondary,
                modifier           = Modifier.size(20.dp)
            )
        }
    }
}

// ── Slide-up filter panel ──────────────────────────────────────────────────────
@Composable
private fun FilterPanel(
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    radiusKm: Float,
    onRadiusChange: (Float) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(SurfacePanel)
            .border(
                width = 1.dp,
                color = ChipBorder,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .navigationBarsPadding()
            .padding(top = 12.dp, bottom = 28.dp, start = 20.dp, end = 20.dp)
    ) {
        // Drag handle
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(ChipBorder)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(20.dp))

        // Header row
        Row(
            modifier       = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text       = "Filters",
                color      = TextPrimary,
                fontSize   = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier   = Modifier.weight(1f)
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close filters", tint = TextSecondary)
            }
        }

        Spacer(Modifier.height(20.dp))

        // Category label
        Text(
            text     = "CATEGORY",
            color    = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.height(10.dp))

        // Category chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ALL_CATEGORIES.forEach { cat ->
                CategoryChip(
                    category  = cat,
                    selected  = selectedCategory == cat.label,
                    onClick   = { onCategorySelect(cat.label) }
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Radius label
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text       = "DISTANCE",
                color      = TextSecondary,
                fontSize   = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.5.sp
            )
            Text(
                text       = "${radiusKm.toInt()} km",
                color      = Accent,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(8.dp))

        Slider(
            value         = radiusKm,
            onValueChange = onRadiusChange,
            valueRange    = 1f..100f,
            steps         = 0,
            colors        = SliderDefaults.colors(
                thumbColor          = Accent,
                activeTrackColor    = Accent,
                inactiveTrackColor  = SliderTrack
            ),
            modifier      = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("1 km",   color = TextSecondary, fontSize = 11.sp)
            Text("100 km", color = TextSecondary, fontSize = 11.sp)
        }
    }
}

// ── Category chip ──────────────────────────────────────────────────────────────
@Composable
private fun CategoryChip(
    category: ItemCategory,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor     = if (selected) AccentMuted   else Color(0xFF2A2926)
    val borderColor = if (selected) Accent         else ChipBorder
    val textColor   = if (selected) Accent         else TextSecondary

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = category.emoji, fontSize = 14.sp)
        Text(
            text       = category.label,
            color      = textColor,
            fontSize   = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// ── Persistent badge (shown when panel is closed) ─────────────────────────────
@Composable
private fun ActiveFilterBadge(
    radiusKm: Float,
    categoryLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val label = if (categoryLabel == "All")
        "Within ${radiusKm.toInt()} km · All categories"
    else
        "Within ${radiusKm.toInt()} km · $categoryLabel"

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(SurfacePanel)
            .border(1.dp, ChipBorder, RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector        = Icons.Default.Tune,
            contentDescription = null,
            tint               = Accent,
            modifier           = Modifier.size(16.dp)
        )
        Text(
            text     = label,
            color    = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}