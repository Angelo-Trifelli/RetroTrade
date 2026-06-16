package com.example.retrotrade.ui.navigation.homepage

import androidx.lifecycle.viewModelScope
import com.example.retrotrade.data.UserSession
import com.example.retrotrade.repository.HomeRepository
import com.example.retrotrade.repository.UserRepository
import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {

    private val homeRepository = HomeRepository()
    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    //Separate loading/error state so the UI can react without inspecting every field of HomeUiState
    private val _dataState = MutableStateFlow<GenericUiState>(GenericUiState.Idle)
    val dataState: StateFlow<GenericUiState> = _dataState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch(Dispatchers.IO) {
            _dataState.value = GenericUiState.Loading

            val username = UserSession.currentUser
                ?.username
                ?.takeIf { it.isNotBlank() }
                ?: "Collector"

            try {
                val statsDefer = async(Dispatchers.IO) { userRepository.getStats() }
                val recentItemsDefer = async(Dispatchers.IO) { homeRepository.getRecentItems() }
                val trendingItemsDefer = async(Dispatchers.IO) { homeRepository.getTrendingItems() }

                val userStatsResponse = statsDefer.await().getOrThrow()
                val recentItems = recentItemsDefer.await().getOrThrow()
                val trendingItems = trendingItemsDefer.await()

                _uiState.update {
                    it.copy(
                        username = username,
                        collectionCount = userStatsResponse.activeItems,
                        pendingTradesCount = userStatsResponse.pendingTrades,
                        recentItems = recentItems,
                        trendingItems = trendingItems
                    )
                }

                _dataState.value = GenericUiState.Success
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        username = username,
                        collectionCount = 0,
                        pendingTradesCount = 0,
                        recentItems = emptyList(),
                        trendingItems = emptyList()
                    )
                }

                _dataState.value = GenericUiState.Error(e.message ?: "Failed to load home data")
            }
        }
    }

    fun resetDataState() {
        _dataState.value = GenericUiState.Idle
    }

    fun onFindTrades() {
        navigate(Screen.Map.route)
    }
}
