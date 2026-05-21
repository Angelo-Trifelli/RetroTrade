package com.example.retrotrade.ui.screens.scan

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrotrade.ui.navigation.scan.ItemCategory
import com.example.retrotrade.ui.navigation.scan.ScanUiState
import com.example.retrotrade.ui.navigation.scan.availableIconChars
import com.example.retrotrade.ui.theme.BackgroundColor
import com.example.retrotrade.ui.theme.RetroIcon
import com.example.retrotrade.ui.theme.RetroOrange
import com.example.retrotrade.ui.theme.RetroTextPrimary
import com.example.retrotrade.ui.theme.RetroTextSecondary


@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview(showBackground = true)
fun ScanContent(
    modifier: Modifier = Modifier,
    uiState: ScanUiState = ScanUiState(),
    onPhotoCaptured: (Bitmap?) -> Unit = {},
    onClearPhoto: () -> Unit = {},
    onItemNameChanged: (String) -> Unit = {},
    onCategorySelected: (ItemCategory) -> Unit = {},
    onCategoryDropdownToggle: () -> Unit = {},
    onCategoryDropdownDismiss: () -> Unit = {},
    onEstimatedValueChanged: (String) -> Unit = {},
    onIconCharSelected: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
    onSuccessDismissed: () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Header ──────────────────────────────────────────
            ScanHeader()

            Spacer(modifier = Modifier.height(16.dp))

            // ── Photo capture area ──────────────────────────────
            PhotoCaptureSection(
                capturedPhoto = uiState.capturedPhoto,
                onPhotoCaptured = onPhotoCaptured,
                onClearPhoto = onClearPhoto
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Item name ───────────────────────────────────────
            SectionLabel(label = "Item Name")
            Spacer(modifier = Modifier.height(8.dp))
            ItemNameField(
                value = uiState.itemName,
                onValueChanged = onItemNameChanged
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Category selector ───────────────────────────────
            SectionLabel(label = "Category")
            Spacer(modifier = Modifier.height(8.dp))
            CategorySelector(
                selectedCategory = uiState.selectedCategory,
                isExpanded = uiState.isCategoryDropdownExpanded,
                onToggle = onCategoryDropdownToggle,
                onDismiss = onCategoryDropdownDismiss,
                onCategorySelected = onCategorySelected
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Estimated value ─────────────────────────────────
            SectionLabel(label = "Estimated Value")
            Spacer(modifier = Modifier.height(8.dp))
            EstimatedValueField(
                value = uiState.estimatedValue,
                onValueChanged = onEstimatedValueChanged
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Icon char picker ────────────────────────────────
            SectionLabel(label = "Icon")
            Spacer(modifier = Modifier.height(8.dp))
            IconCharPicker(
                selectedIconChar = uiState.selectedIconChar,
                onIconCharSelected = onIconCharSelected
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Submit button ───────────────────────────────────
            SubmitButton(
                isEnabled = uiState.isFormValid,
                isLoading = uiState.isSubmitting,
                onClick = onSubmit
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // ── Success overlay ─────────────────────────────────────
        AnimatedVisibility(
            visible = uiState.showSuccessMessage,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut()
        ) {
            SuccessOverlay(onDismiss = onSuccessDismissed)
        }
    }
}


// ─── Header ─────────────────────────────────────────────────────────
@Composable
private fun ScanHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Scan Item",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = RetroTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Add a new item to your collection",
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
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                tint = RetroOrange,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


// ─── Section label ──────────────────────────────────────────────────
@Composable
private fun SectionLabel(label: String) {
    Text(
        text = label,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = RetroTextPrimary,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
}


// ─── Photo capture ──────────────────────────────────────────────────
@Composable
private fun PhotoCaptureSection(
    capturedPhoto: Bitmap?,
    onPhotoCaptured: (Bitmap?) -> Unit,
    onClearPhoto: () -> Unit
) {
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        onPhotoCaptured(bitmap)
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (capturedPhoto != null) {
                // Show captured photo
                Image(
                    bitmap = capturedPhoto.asImageBitmap(),
                    contentDescription = "Captured item photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Clear button overlay
                IconButton(
                    onClick = onClearPhoto,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .background(
                            Color.Black.copy(alpha = 0.5f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove photo",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                // Empty state – tap to capture
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundColor)
                        .clickable { cameraLauncher.launch(null) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(Color(0xFFD35400), Color(0xFFE67E22))
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Take photo",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Tap to take a photo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = RetroTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Use your camera to capture the item",
                        fontSize = 13.sp,
                        color = RetroTextSecondary
                    )
                }
            }
        }
    }
}


// ─── Item name field ────────────────────────────────────────────────
@Composable
private fun ItemNameField(
    value: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        placeholder = {
            Text("e.g. Charizard Holo 1st Edition", color = RetroIcon)
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


// ─── Category selector ──────────────────────────────────────────────
@Composable
private fun CategorySelector(
    selectedCategory: ItemCategory?,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onDismiss: () -> Unit,
    onCategorySelected: (ItemCategory) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        OutlinedTextField(
            value = selectedCategory?.label ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text("Select a category", color = RetroIcon)
            },
            trailingIcon = {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle dropdown",
                    tint = RetroIcon,
                    modifier = Modifier.clickable { onToggle() }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() },
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

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismiss,
            modifier = Modifier.background(Color.White)
        ) {
            ItemCategory.entries.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.label,
                            color = if (selectedCategory == category) RetroOrange
                            else RetroTextPrimary,
                            fontWeight = if (selectedCategory == category) FontWeight.Bold
                            else FontWeight.Normal
                        )
                    },
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}


// ─── Estimated value field ──────────────────────────────────────────
@Composable
private fun EstimatedValueField(
    value: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        placeholder = {
            Text("0.00", color = RetroIcon)
        },
        leadingIcon = {
            Text(
                text = "$",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = RetroOrange
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            focusedBorderColor = RetroOrange,
            unfocusedBorderColor = BackgroundColor,
            cursorColor = RetroOrange
        )
    )
}


// ─── Icon char picker ───────────────────────────────────────────────
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IconCharPicker(
    selectedIconChar: String?,
    onIconCharSelected: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            availableIconChars.forEach { iconChar ->
                val isSelected = selectedIconChar == iconChar
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) RetroOrange.copy(alpha = 0.15f)
                    else BackgroundColor,
                    label = "iconBg"
                )
                val borderColor by animateColorAsState(
                    targetValue = if (isSelected) RetroOrange else Color.Transparent,
                    label = "iconBorder"
                )

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(bgColor)
                        .border(
                            width = 2.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onIconCharSelected(iconChar) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = iconChar, fontSize = 24.sp)
                }
            }
        }
    }
}


// ─── Submit button ──────────────────────────────────────────────────
@Composable
private fun SubmitButton(
    isEnabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = isEnabled && !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = RetroOrange,
            disabledContainerColor = RetroOrange.copy(alpha = 0.4f)
        )
    ) {
        if (isLoading) {
            Text(
                text = "Listing...",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        } else {
            Text(
                text = "List Item",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}


// ─── Success overlay ────────────────────────────────────────────────
@Composable
private fun SuccessOverlay(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(48.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(Color(0xFF27AE60), Color(0xFF2ECC71))
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Item Listed!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = RetroTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your item has been added to your collection",
                    fontSize = 14.sp,
                    color = RetroTextSecondary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = RetroOrange),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Scan Another",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
