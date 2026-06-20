package com.example.retrotrade.ui.screens.trades

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrotrade.model.ChatMessage
import com.example.retrotrade.model.Trade
import com.example.retrotrade.model.TradeStatus
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.trades.TradeChatUiState
import com.example.retrotrade.ui.theme.BackgroundColor
import com.example.retrotrade.ui.theme.RetroOrange
import com.example.retrotrade.ui.theme.RetroTextPrimary
import com.example.retrotrade.ui.theme.RetroTextSecondary


// ─── Screen ───────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun TradeChatContent(
    uiState: TradeChatUiState = TradeChatUiState(),
    dataState: GenericUiState = GenericUiState.Idle,
    onBack: () -> Unit = {},
    onSendMessage: (String) -> Unit = {},
    onCompleteTrade: () -> Unit = {},
    onAcceptTrade: () -> Unit = {},
    onRejectTrade: () -> Unit = {}
) {
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) listState.animateScrollToItem(uiState.messages.size - 1)
    }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TradeChatTopBar(trade = uiState.trade, onBack = onBack)
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                if (uiState.trade.id.isNotEmpty() && (uiState.trade.status == TradeStatus.PENDING || uiState.trade.status == TradeStatus.ACCEPTED)) {
                    // ── Trade action buttons (only for pending/active) ──
                    TradeActionBar(
                        trade = uiState.trade,
                        onComplete = onCompleteTrade,
                        onAccept = onAcceptTrade,
                        onReject = onRejectTrade
                    )

                    // ── Message input ──────────────────────────────────
                    MessageInputBar(onSend = onSendMessage)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ── Item summary card ──────────────────────────────────
            ItemSummaryBanner(trade = uiState.trade)

            // ── Messages list ──────────────────────────────────────
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(uiState.messages, key = { it.id }) { message ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 })
                    ) {
                        MessageBubble(message = message)
                    }
                }
            }
        }
    }
}

// ─── Top Bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TradeChatTopBar(trade: Trade, onBack: () -> Unit) {
    val otherParty = if (trade.isBuying) trade.sellerName else trade.buyerName
    val initials = otherParty.take(2).uppercase()

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = RetroTextPrimary
        ),
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = RetroTextPrimary
                )
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // ── Avatar ─────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(RetroOrange.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetroOrange
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = otherParty,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetroTextPrimary
                    )
                    Text(
                        text = if (trade.isBuying) "Seller" else "Buyer",
                        fontSize = 12.sp,
                        color = RetroTextSecondary
                    )
                }
            }
        },
        actions = {
            // ── Status pill ────────────────────────────────────────
            val (bgColor, textColor) = when (trade.status) {
                TradeStatus.PENDING -> Color(0xFFFFF3E0) to Color(0xFFE65100)
                TradeStatus.ACCEPTED -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
                TradeStatus.COMPLETED -> Color(0xFFF3E5F5) to Color(0xFF7B1FA2)
                TradeStatus.REJECTED -> Color(0xFFFFEBEE) to Color(0xFFC62828)
            }
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .background(bgColor, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = trade.status.label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
        }
    )
}

// ─── Item summary banner ──────────────────────────────────────────────────────

@Composable
private fun ItemSummaryBanner(trade: Trade) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Emoji icon ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = trade.itemIcon, fontSize = 24.sp)
            }

            Spacer(Modifier.width(12.dp))

            // ── Item info ──────────────────────────────────────────
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trade.itemName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetroTextPrimary
                )
                Spacer(Modifier.height(2.dp))
                Row {
                    Text(
                        text = "Ask: ${trade.requestedPrice}",
                        fontSize = 12.sp,
                        color = RetroTextSecondary
                    )
                    Text(
                        text = "  ·  ",
                        fontSize = 12.sp,
                        color = RetroTextSecondary
                    )
                    Text(
                        text = "Offer: ${trade.offeredPrice}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RetroOrange
                    )
                }
            }
        }
    }
}

// ─── Trade action bar ─────────────────────────────────────────────────────────

@Composable
private fun TradeActionBar(
    trade: Trade,
    onComplete: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    // Only the seller sees accept/reject buttons on an incoming offer
    if (!trade.isBuying) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFAFAFA))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ── Reject ─────────────────────────────────────────────
            TextButton(
                onClick = onReject,
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFFFEBEE), RoundedCornerShape(12.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color(0xFFC62828),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Decline",
                    color = Color(0xFFC62828),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }

            // ── Accept ─────────────────────────────────────────────
            if (trade.status == TradeStatus.PENDING) {
                TextButton(
                    onClick = onAccept,
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF1565C0), RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Accept",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }

            // ── Complete ─────────────────────────────────────────────
            if (trade.status == TradeStatus.ACCEPTED) {
                TextButton(
                    onClick = onComplete,
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF2E7D32), RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Complete",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

// ─── Message input ────────────────────────────────────────────────────────────

@Composable
private fun MessageInputBar(onSend: (String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = {
                Text(
                    text = "Type a message…",
                    fontSize = 14.sp,
                    color = RetroTextSecondary
                )
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RetroOrange,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                cursorColor = RetroOrange,
                focusedTextColor = RetroTextPrimary,
                unfocusedTextColor = RetroTextPrimary,
                unfocusedContainerColor = BackgroundColor,
                focusedContainerColor = Color.White
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (text.isNotBlank()) {
                        onSend(text.trim())
                        text = ""
                    }
                }
            )
        )

        Spacer(Modifier.width(8.dp))

        // ── Send button ────────────────────────────────────────────
        val canSend = text.isNotBlank()
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(if (canSend) RetroOrange else Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    if (canSend) {
                        onSend(text.trim())
                        text = ""
                    }
                },
                enabled = canSend
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ─── Message bubble ───────────────────────────────────────────────────────────

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isMe = message.isFromMe

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        // ── Other user avatar ──────────────────────────────────────
        if (!isMe) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(RetroOrange.copy(alpha = 0.12f))
                    .align(Alignment.Bottom),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.senderName.take(1).uppercase(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetroOrange
                )
            }
            Spacer(Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 270.dp)
        ) {
            // ── Bubble ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (isMe) 18.dp else 4.dp,
                            bottomEnd = if (isMe) 4.dp else 18.dp
                        )
                    )
                    .background(if (isMe) RetroOrange else Color.White)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = if (isMe) Color.White else RetroTextPrimary,
                    lineHeight = 20.sp
                )
            }

            Spacer(Modifier.height(2.dp))

            // ── Timestamp ──────────────────────────────────────────
            Text(
                text = message.timestamp,
                fontSize = 11.sp,
                color = RetroTextSecondary
            )
        }
    }
}