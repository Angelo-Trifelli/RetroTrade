package com.example.retrotrade.ui.navigation.map

import com.example.retrotrade.model.ItemCategory
import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.GenericUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    //Separate loading/error state so the UI can react without inspecting every field of MapUiState
    private val _dataState = MutableStateFlow<GenericUiState>(GenericUiState.Idle)
    val dataState: StateFlow<GenericUiState> = _dataState.asStateFlow()


    fun onBack() {
        popBackStack()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onItemCategoryChange(category: ItemCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onRadiusChange(radiusKm: Float) {
        _uiState.value = _uiState.value.copy(radiusKm = radiusKm)
    }

    fun onFilterWindowClosed() {

    }

    fun resetDataState() {
        _dataState.value = GenericUiState.Idle
    }
}