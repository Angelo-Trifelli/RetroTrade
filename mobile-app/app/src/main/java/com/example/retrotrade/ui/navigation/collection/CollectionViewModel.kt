package com.example.retrotrade.ui.navigation.collection

import androidx.lifecycle.viewModelScope
import com.example.retrotrade.model.CollectionListItem
import com.example.retrotrade.repository.ItemRepository
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

class CollectionViewModel : BaseViewModel() {

    private val itemRepository = ItemRepository()

    private val _uiState = MutableStateFlow(CollectionUiState())
    val uiState: StateFlow<CollectionUiState> = _uiState.asStateFlow()

    //Separate loading/error state so the UI can react without inspecting every field of CollectionUiState
    private val _dataState = MutableStateFlow<GenericUiState>(GenericUiState.Idle)
    val dataState: StateFlow<GenericUiState> = _dataState.asStateFlow()

    /* --------------------------- CONSTRUCTOR --------------------------- */
    init {
        loadCollectionData()
    }

    /* --------------------------- PUBLIC API --------------------------- */
    fun onRefresh() {
        loadCollectionData()
    }

    fun onFilterSelected(filter: CollectionFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onItemSelected(itemId: String) {
        navigate(Screen.ItemDetails.createRoute(itemId, source = Screen.Collection.route))
    }

    fun resetDataState() {
        _dataState.value = GenericUiState.Idle
    }

    /* ----------------------- PRIVATE FUNCTIONS ------------------------ */
    private fun loadCollectionData() {
        viewModelScope.launch {
            _dataState.value = GenericUiState.Loading

            try {
                val itemsDefer = async(Dispatchers.IO) { itemRepository.loadItems() }

                val itemsResponse = itemsDefer.await().getOrThrow()

                _uiState.update {
                    it.copy(
                        searchQuery = it.searchQuery,
                        selectedFilter = it.selectedFilter,
                        items = itemsResponse.map { item ->
                            CollectionListItem(
                                id = item.id,
                                name = item.name,
                                category = item.category,
                                estimatedValue = item.estimatedValue,
                                iconChar = item.iconChar,
                                status = item.status
                            )
                        }
                    )
                }

                _dataState.value = GenericUiState.Success
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        searchQuery = "",
                        selectedFilter = CollectionFilter.ALL,
                        items = emptyList()
                    )
                }

                _dataState.value = GenericUiState.Error(e.message ?: "Failed to load collection data")
            }
        }
    }
}
