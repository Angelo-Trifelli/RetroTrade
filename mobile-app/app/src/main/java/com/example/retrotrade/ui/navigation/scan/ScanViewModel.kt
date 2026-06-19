package com.example.retrotrade.ui.navigation.scan

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrotrade.model.ItemCategory
import com.example.retrotrade.repository.ItemRepository
import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.NavEvent
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

// ─── Icon char options ──────────────────────────────────────────────
val availableIconChars = listOf(
    "🔥", "🎮", "👖", "🕹️", "⚡", "🧥", "🎵", "📀",
    "🃏", "🎲", "🧸", "📷", "💎", "🏆", "🎸", "📚"
)

class ScanViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val itemRepository = ItemRepository()

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    //Separate loading/error state so the UI can react without inspecting every field of ScanUiState
    private val _dataState = MutableStateFlow<GenericUiState>(GenericUiState.Idle)
    val dataState: StateFlow<GenericUiState> = _dataState.asStateFlow()

    private val _navEvent = MutableSharedFlow<NavEvent>()
    val navEvent = _navEvent.asSharedFlow()


    /* --------------------------- PUBLIC API --------------------------- */
    fun onPhotoCaptured(bitmap: Bitmap?) {
        _uiState.value = _uiState.value.copy(capturedPhoto = bitmap)
    }

    fun onItemNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(itemName = name)
    }

    fun onCategorySelected(category: ItemCategory) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category
        )
    }

    fun onEstimatedValueChanged(value: String) {
        // Only allow numeric input with optional decimal
        val filtered = value.filter { it.isDigit() || it == '.' }
        _uiState.value = _uiState.value.copy(estimatedValue = filtered)
    }

    fun onIconCharSelected(iconChar: String) {
        _uiState.value = _uiState.value.copy(selectedIconChar = iconChar)
    }

    fun onSubmit() {
        if (!_uiState.value.isFormValid) return

        _dataState.value = GenericUiState.Loading

        viewModelScope.launch {
            val location = getCurrentLocation()
            if (location == null) {
                _dataState.value = GenericUiState.Error("Unable to get your location. Please try again.")
                return@launch
            }

            itemRepository.createItem(
                _uiState.value.base64Photo,
                _uiState.value.itemName,
                _uiState.value.selectedCategory!!,
                _uiState.value.estimatedValue,
                _uiState.value.selectedIconChar!!,
                location.latitude,
                location.longitude
            ).onSuccess {
                _dataState.value = GenericUiState.Success
            }.onFailure {
                _dataState.value = GenericUiState.Error(it.message ?: "Failed to create item")
            }
        }
    }

    fun onSuccessDismissed() {
        _uiState.value = ScanUiState()
        _dataState.value = GenericUiState.Idle
    }

    fun onClearPhoto() {
        _uiState.value = _uiState.value.copy(capturedPhoto = null)
    }

    fun resetDataState() {
        _dataState.value = GenericUiState.Idle
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation(): LatLng? {
        val client = LocationServices.getFusedLocationProviderClient(getApplication())
        return suspendCancellableCoroutine { cont ->
            client.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { location ->
                if (cont.isActive) cont.resume(location?.let { LatLng(it.latitude, it.longitude) }) { _, _, _ -> }
            }.addOnFailureListener {
                if (cont.isActive) cont.resume(null) { _, _, _ -> }
            }
        }
    }


    private fun navigate(route: String) {
        viewModelScope.launch {
            _navEvent.emit(NavEvent.Navigate(route))
        }
    }

    private fun popBackStack() {
        viewModelScope.launch {
            _navEvent.emit(NavEvent.PopBackStack)
        }
    }
}
