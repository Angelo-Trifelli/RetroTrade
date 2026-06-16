package com.example.retrotrade.ui.screens.scan

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrotrade.model.ItemCategory
import com.example.retrotrade.ui.components.common.AppHeader
import com.example.retrotrade.ui.components.common.SectionHeader
import com.example.retrotrade.ui.components.common.SelectField
import com.example.retrotrade.ui.components.scan.PhotoCaptureSection
import com.example.retrotrade.ui.components.scan.SubmitButton
import com.example.retrotrade.ui.navigation.GenericUiState
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
    dataState: GenericUiState = GenericUiState.Idle,
    onPhotoCaptured: (Bitmap?) -> Unit = {},
    onClearPhoto: () -> Unit = {},
    onItemNameChanged: (String) -> Unit = {},
    onCategorySelected: (ItemCategory) -> Unit = {},
    onEstimatedValueChanged: (String) -> Unit = {},
    onIconCharSelected: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
    onSuccessDismissed: () -> Unit = {}
) {

    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Header ──────────────────────────────────────────
            AppHeader(
                title = "Scan Item",
                subtitle = "Add a new item to your collection",
                icon = Icons.Default.CameraAlt
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Photo capture area ──────────────────────────────
            PhotoCaptureSection(
                capturedPhoto = uiState.capturedPhoto,
                onPhotoCaptured = onPhotoCaptured,
                onClearPhoto = onClearPhoto
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Item name ───────────────────────────────────────
            SectionHeader(title = "Item Name")

            Spacer(modifier = Modifier.height(8.dp))

            ItemNameField(
                value = uiState.itemName,
                onValueChanged = onItemNameChanged
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Category selector ───────────────────────────────
            SectionHeader(title = "Category")

            Spacer(modifier = Modifier.height(8.dp))

            SelectField(
                placeholder = "Select a category",
                menuOptions = ItemCategory.entries.toList().filter { it != ItemCategory.ALL },
                selectedOption = uiState.selectedCategory,
                onOptionSelected = {categorySelected ->
                    if (categorySelected is ItemCategory) {
                        onCategorySelected(categorySelected)
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Estimated value ─────────────────────────────────
            SectionHeader(title = "Estimated Value")

            Spacer(modifier = Modifier.height(8.dp))

            EstimatedValueField(
                value = uiState.estimatedValue,
                onValueChanged = onEstimatedValueChanged
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Icon char picker ────────────────────────────────
            SectionHeader(title = "Icon Char")

            Spacer(modifier = Modifier.height(8.dp))

            IconCharPicker(
                selectedIconChar = uiState.selectedIconChar,
                onIconCharSelected = onIconCharSelected
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Submit button ───────────────────────────────────
            SubmitButton(
                isEnabled = uiState.isFormValid,
                isLoading = dataState is GenericUiState.Loading,
                onClick = onSubmit
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // ── Success overlay ─────────────────────────────────────
        AnimatedVisibility(
            visible = dataState is GenericUiState.Success,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut()
        ) {
            SuccessOverlay(onDismiss = onSuccessDismissed)
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
                text = "€",
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
