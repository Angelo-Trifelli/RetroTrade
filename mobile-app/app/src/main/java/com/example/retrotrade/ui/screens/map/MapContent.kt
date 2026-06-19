package com.example.retrotrade.ui.screens.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.retrotrade.model.ItemCategory
import com.example.retrotrade.model.toClusterItem
import com.example.retrotrade.rest.model.response.LoadItemsResponse
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.map.MapUiState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState

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


@OptIn(MapsComposeExperimentalApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapContent(
    uiState: MapUiState       = MapUiState(),
    dataState: GenericUiState = GenericUiState.Idle,
    userLocation: LatLng?     = null,
    lastCameraPosition: CameraPosition? = null,
    onBack: () -> Unit        = {},
    onSearchQueryChange: (String) -> Unit = {},
    onItemCategoryChange: (ItemCategory) -> Unit = {},
    onRadiusChange: (Float) -> Unit = {},
    onFilterWindowClosed: () -> Unit,
    onClusterClick: (List<LoadItemsResponse>) -> Unit = {},
    onClusterDismiss: () -> Unit       = {},
    onMapItemSelected: (String, CameraPosition) -> Unit
) {

    var filtersExpanded by remember { mutableStateOf(false) }
    var locationCentered by remember { mutableStateOf(lastCameraPosition != null) } //skip centering if we have already a position
    var locationPermissionGranted by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        lastCameraPosition?.let { position = it }
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (fine == PackageManager.PERMISSION_GRANTED && coarse == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(userLocation) {
        if (!locationCentered) {
            userLocation?.let { pos ->
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(pos, 15f),  // zoom 15 = street level
                    durationMs = 800
                )
            }
            if (userLocation != null) {
                locationCentered = true
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        // ── Map ───────
        GoogleMap(
            modifier            = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties          = MapProperties(isMyLocationEnabled = false), // we draw our own dot
            uiSettings          = MapUiSettings(myLocationButtonEnabled = false),
            onMapLoaded         = { }
        ) {
            // Custom user-location dot
            userLocation?.let { pos ->
                UserLocationDot(position = pos)
            }

            Clustering(
                items = uiState.items.map { it.toClusterItem() },
                onClusterClick = { cluster ->
                    onClusterClick(cluster.items.map { it.item })
                    false
                },
                onClusterItemClick = { clusterItem ->
                    onClusterClick(listOf(clusterItem.item))
                    false
                },
                clusterContent = { cluster ->
                    ClusterBubble(count = cluster.size)
                },
                clusterItemContent = { clusterItem ->
                    ItemMarkerBubble(item = clusterItem.item)
                }
            )
        }

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

        // ── Cluster bottom sheet ───────────────────────────────────────────
        if (uiState.selectedClusterItems.isNotEmpty()) {
            ModalBottomSheet(
                onDismissRequest   = onClusterDismiss,
                sheetState         = sheetState,
                containerColor     = Surface,
                contentColor       = TextPrimary,
                shape              = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                ClusterBottomSheet(
                    items     = uiState.selectedClusterItems,
                    onDismiss = onClusterDismiss,
                    onMapItemSelected = { itemId -> onMapItemSelected(itemId, cameraPositionState.position)}
                )
            }
        }
    }
}


@Composable
private fun UserLocationDot(position: LatLng) {
    // Outer pulse ring
    Circle(
        center      = position,
        radius      = 40.0,           // metres
        fillColor   = Accent.copy(alpha = 0.15f),
        strokeColor = Accent.copy(alpha = 0.35f),
        strokeWidth = 2f
    )
    // Solid centre dot
    Circle(
        center      = position,
        radius      = 10.0,
        fillColor   = Accent,
        strokeColor = Color.White,
        strokeWidth = 3f
    )
}

@Composable
private fun ClusterBubble(count: Int) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Accent)
            .border(2.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = if (count > 99) "99+" else count.toString(),
            color      = Color.White,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ItemMarkerBubble(item: LoadItemsResponse) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfacePanel)
            .border(1.dp, Accent, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = item.iconChar, fontSize = 18.sp)
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

@Composable
private fun ClusterBottomSheet(
    items    : List<LoadItemsResponse>,
    onDismiss: () -> Unit,
    onMapItemSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
    ) {
        // Header
        Row(
            modifier      = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text       = if (items.size == 1) "1 item here" else "${items.size} items here",
                color      = TextPrimary,
                fontSize   = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier   = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
            }
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = ChipBorder)
        Spacer(Modifier.height(12.dp))

        // Item list
        items.forEach { item ->
            ClusterItemRow(
                item = item,
                onMapItemSelected = { itemId ->
                    onDismiss()
                    onMapItemSelected(itemId)
                }
            )
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ClusterItemRow(
    item: LoadItemsResponse,
    onMapItemSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF2A2926))
            .border(1.dp, ChipBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
            .clickable{ onMapItemSelected(item.id) },
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Icon bubble
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AccentMuted)
                .border(1.dp, Accent, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.iconChar, fontSize = 20.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = item.name,
                color      = TextPrimary,
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text     = item.category.label,
                color    = TextSecondary,
                fontSize = 12.sp
            )
        }

        Text(
            text       = "~€${item.estimatedValue}",
            color      = Accent,
            fontSize   = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}