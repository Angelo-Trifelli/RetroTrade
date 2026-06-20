package com.example.retrotrade.ui.navigation.trades

import androidx.lifecycle.viewModelScope
import com.example.retrotrade.model.CollectionListItem
import com.example.retrotrade.model.Trade
import com.example.retrotrade.repository.TradeRepository
import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.Screen
import com.example.retrotrade.ui.screens.collection.CollectionFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TradesViewModel : BaseViewModel() {

    private val tradeRepository = TradeRepository()

    private val _uiState = MutableStateFlow(TradesUiState())
    val uiState: StateFlow<TradesUiState> = _uiState.asStateFlow()

    //Separate loading/error state so the UI can react without inspecting every field of TradesUiState
    private val _dataState = MutableStateFlow<GenericUiState>(GenericUiState.Idle)
    val dataState: StateFlow<GenericUiState> = _dataState.asStateFlow()


    /* --------------------------- CONSTRUCTOR --------------------------- */
    init {
        loadTradesData()
    }

    /* --------------------------- PUBLIC API --------------------------- */
    fun onRefresh() {
        loadTradesData()
    }

    fun onTabSelected(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = index)
    }

    fun onOpenChat(trade: Trade) {
        navigate(Screen.TradeChat.createRoute(trade.id))
    }


    fun resetDataState() {
        _dataState.value = GenericUiState.Idle
    }

    /* ----------------------- PRIVATE FUNCTIONS ------------------------ */
    private fun loadTradesData() {
        viewModelScope.launch {
            _dataState.value = GenericUiState.Loading

            try {
                val tradesDefer = async(Dispatchers.IO) { tradeRepository.loadTrades() }

                val tradesResponse = tradesDefer.await().getOrThrow()

                _uiState.update {
                    it.copy(
                        selectedTabIndex = it.selectedTabIndex,
                        trades = tradesResponse
                    )
                }

                _dataState.value = GenericUiState.Success
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        selectedTabIndex = 0,
                        trades = emptyList()
                    )
                }

                _dataState.value = GenericUiState.Error(e.message ?: "Failed to load trades data")
            }
        }
    }
}


