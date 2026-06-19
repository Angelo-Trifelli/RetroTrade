package com.example.retrotrade.ui.screens.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.item.ItemDetailsUiState
import com.example.retrotrade.ui.theme.BackgroundColor
import com.example.retrotrade.ui.theme.RetroOrange
import com.example.retrotrade.ui.theme.RetroTextPrimary
import com.example.retrotrade.ui.theme.RetroTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun ItemDetailsContent(
    uiState: ItemDetailsUiState = ItemDetailsUiState(),
    dataState: GenericUiState = GenericUiState.Idle,
    onGoBack: () -> Unit = {}
) {

    var showOfferSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Item Details", color = RetroTextPrimary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor
                )
            )
        },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            if (dataState is GenericUiState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = RetroOrange)
                }
            } else if (uiState.item != null) {
                val item = uiState.item!!

                // Photo Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.photoBitmap != null) {
                        Image(
                            bitmap = uiState.photoBitmap!!.asImageBitmap(),
                            contentDescription = "Item Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // Fallback icon if no photo available
                        Text(text = item.iconChar, fontSize = 80.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Item Details
                Text(
                    text = item.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetroTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Category: ${item.category.label}",
                    fontSize = 16.sp,
                    color = RetroTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Status: ${item.status.label}",
                    fontSize = 16.sp,
                    color = RetroTextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${item.estimatedValue} €",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RetroOrange
                )


                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.isFromMap) {
                    Button(
                        onClick = { showOfferSheet = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RetroOrange)
                    ) {
                        Icon(Icons.Outlined.LocalOffer, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Make an Offer", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }

        if (showOfferSheet) {
            MakeOfferBottomSheet(
                itemName = uiState.item?.name!!,
                estimatedValue = uiState.item?.estimatedValue!!,
                onDismiss = { showOfferSheet = false },
                onSubmitOffer = { amount, message ->
                    showOfferSheet = false
                    // TODO: forward to ViewModel
                }
            )
        }
    }


}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeOfferBottomSheet(
    itemName: String,
    estimatedValue: String,
    onDismiss: () -> Unit,
    onSubmitOffer: (amount: String, message: String?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var offerAmount by remember { mutableStateOf("") }
    var offerMessage by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF1C1B1F),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Make an Offer",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = itemName,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Estimated value hint pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFD4820A).copy(alpha = 0.12f))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFD4820A).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Color(0xFFD4820A),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Estimated value: ${estimatedValue.toDoubleOrNull()?.toInt() ?: 0} €",
                    fontSize = 13.sp,
                    color = Color(0xFFD4820A)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Amount field
            val amountFocused = remember { MutableInteractionSource() }
            val isAmountFocused by amountFocused.collectIsFocusedAsState()

            Text(
                text = "Your offer",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .border(
                        width = 1.5.dp,
                        color = when {
                            amountError -> Color(0xFFE57373)
                            isAmountFocused -> Color(0xFFD4820A)
                            else -> Color.White.copy(alpha = 0.12f)
                        },
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "€",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isAmountFocused) Color(0xFFD4820A) else Color.White.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = offerAmount,
                        onValueChange = {
                            offerAmount = it.filter { c -> c.isDigit() || c == '.' }
                            amountError = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Offer Amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        interactionSource = amountFocused,
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            if (amountError) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Please enter a valid amount",
                    fontSize = 12.sp,
                    color = Color(0xFFE57373)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Optional message field
            Text(
                text = "MESSAGE  ·  OPTIONAL",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            val messageFocused = remember { MutableInteractionSource() }
            val isMessageFocused by messageFocused.collectIsFocusedAsState()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .border(
                        width = 1.5.dp,
                        color = if (isMessageFocused) Color(0xFFD4820A)
                        else Color.White.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                OutlinedTextField(
                    value = offerMessage,
                    onValueChange = { if (it.length <= 200) offerMessage = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp),
                    label = { Text("Add a note to the seller…", fontSize = 15.sp, color = Color.White, lineHeight = 22.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    interactionSource = messageFocused,
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${offerMessage.length}/200",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.padding(top = 4.dp, end = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Submit button
            Button(
                onClick = {
                    val amount = offerAmount.toDoubleOrNull()
                    if (amount == null || amount <= 0.0) {
                        amountError = true
                    } else {
                        onSubmitOffer(offerAmount, offerMessage.takeIf { it.isNotBlank() })
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD4820A)
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocalOffer,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Send Offer",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}