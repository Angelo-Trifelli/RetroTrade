package com.example.retrotrade.ui.navigation.trades

import com.example.retrotrade.model.ChatMessage
import com.example.retrotrade.model.Trade
import com.example.retrotrade.model.TradeStatus

data class TradeChatUiState(
    val trade: Trade = Trade(),
    val messages: List<ChatMessage> = emptyList()
)


// ─── Sample preview data ─────────────────────────────────────────────────────

private val previewMessages = listOf(
    ChatMessage(
        id = "1",
        senderId = "other",
        senderName = "Marco",
        text = "Hi! I'm interested in your vintage Olympus camera. Is it still available?",
        timestamp = "10:21 AM",
        isFromMe = false
    ),
    ChatMessage(
        id = "2",
        senderId = "me",
        senderName = "Me",
        text = "Yes it is! Great condition, original leather case included.",
        timestamp = "10:23 AM",
        isFromMe = true
    ),
    ChatMessage(
        id = "3",
        senderId = "other",
        senderName = "Marco",
        text = "Amazing. Would you consider €85 instead of €100?",
        timestamp = "10:25 AM",
        isFromMe = false
    ),
    ChatMessage(
        id = "4",
        senderId = "me",
        senderName = "Me",
        text = "I could do €90, final offer. Includes shipping within Italy.",
        timestamp = "10:27 AM",
        isFromMe = true
    ),
    ChatMessage(
        id = "5",
        senderId = "other",
        senderName = "Marco",
        text = "Deal! I'll send payment now.",
        timestamp = "10:30 AM",
        isFromMe = false
    )
)

private val previewTrade = Trade(
    id = "t1",
    itemName = "Olympus OM-1 Camera",
    itemIcon = "📷",
    sellerName = "Marco",
    buyerName = "Me",
    offeredPrice = "€90",
    requestedPrice = "€100",
    lastMessage = "Deal! I'll send payment now.",
    timeAgo = "2h ago",
    status = TradeStatus.ACCEPTED,
    isBuying = true
)