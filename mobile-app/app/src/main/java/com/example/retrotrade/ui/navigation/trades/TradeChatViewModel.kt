package com.example.retrotrade.ui.navigation.trades

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.retrotrade.model.Trade
import com.example.retrotrade.model.TradeStatus
import com.example.retrotrade.repository.TradeRepository
import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.GenericUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TradeChatViewModel(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val tradeRepository = TradeRepository()

    private val _uiState = MutableStateFlow(TradeChatUiState())
    val uiState: StateFlow<TradeChatUiState> = _uiState.asStateFlow()

    private val _dataState = MutableStateFlow<GenericUiState>(GenericUiState.Idle)
    val dataState: StateFlow<GenericUiState> = _dataState.asStateFlow()

    /* --------------------------- CONSTRUCTOR --------------------------- */
    init {
        val tradeId: String? = savedStateHandle.get<String>("tradeId")

        if (tradeId != null) {
            loadChat(tradeId)
        } else {
            _uiState.update {
                it.copy(
                    trade = Trade(),
                    messages = emptyList()
                )
            }

            _dataState.value = GenericUiState.Error("Trade ID is missing")
        }
    }

    /* --------------------------- PUBLIC API --------------------------- */
    fun sendMessage(text: String) {
        viewModelScope.launch {
            _dataState.value = GenericUiState.Loading

            tradeRepository.createTradeMessage(
                _uiState.value.trade.id,
                text
            ).onSuccess { newMessage ->
                if (newMessage != null) {
                    _uiState.value = _uiState.value.copy(messages = _uiState.value.messages + newMessage)
                }

                _dataState.value = GenericUiState.Success
            }.onFailure {
                _dataState.value = GenericUiState.Error(it.message ?: "Failed to send message")
            }
        }
    }

    // ── Trade actions ─────────────────────────────────────────────────────────

    fun acceptTrade() {
        viewModelScope.launch {
            _dataState.value = GenericUiState.Loading

            tradeRepository.acceptTrade(
                _uiState.value.trade.id
            ).onSuccess {
                val updatedTrade = _uiState.value.trade.copy(status = TradeStatus.ACCEPTED)
                _uiState.value = _uiState.value.copy(trade = updatedTrade)
                _dataState.value = GenericUiState.Success
            }.onFailure {
                _dataState.value = GenericUiState.Error(it.message ?: "Failed to send message")
            }
        }
    }

    fun rejectTrade() {
        viewModelScope.launch {
            _dataState.value = GenericUiState.Loading

            tradeRepository.rejectTrade(
                _uiState.value.trade.id
            ).onSuccess {
                val updatedTrade = _uiState.value.trade.copy(status = TradeStatus.REJECTED)
                _uiState.value = _uiState.value.copy(trade = updatedTrade)
                _dataState.value = GenericUiState.Success
            }.onFailure {
                _dataState.value = GenericUiState.Error(it.message ?: "Failed to send message")
            }
        }
    }

    fun completeTrade() {
        viewModelScope.launch {
            _dataState.value = GenericUiState.Loading

            tradeRepository.completeTrade(
                _uiState.value.trade.id
            ).onSuccess {
                val updatedTrade = _uiState.value.trade.copy(status = TradeStatus.COMPLETED)
                _uiState.value = _uiState.value.copy(trade = updatedTrade)
                _dataState.value = GenericUiState.Success
            }.onFailure {
                _dataState.value = GenericUiState.Error(it.message ?: "Failed to send message")
            }
        }
    }

    fun onGoBack() {
        popBackStack()
    }

    fun resetDataState() {
        _dataState.value = GenericUiState.Idle
    }


    /* ----------------------- PRIVATE FUNCTIONS ------------------------ */
    private fun loadChat(tradeId: String) {
        viewModelScope.launch {
            _dataState.value = GenericUiState.Loading
            try {
                val tradeDefer = async(Dispatchers.IO) { tradeRepository.loadTrade(tradeId) }
                val messagesDefer = async(Dispatchers.IO) { tradeRepository.loadTradeMessages(tradeId) }

                val tradeResponse = tradeDefer.await().getOrThrow()
                val messagesResponse = messagesDefer.await().getOrThrow()

                _uiState.update {
                    it.copy(
                        trade = tradeResponse,
                        messages = messagesResponse
                    )
                }

                _dataState.value = GenericUiState.Success
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        trade = Trade(),
                        messages = emptyList()
                    )
                }

                _dataState.value = GenericUiState.Error(e.message ?: "Failed to load chat")
            }
        }
    }
}