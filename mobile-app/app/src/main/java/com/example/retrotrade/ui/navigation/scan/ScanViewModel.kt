package com.example.retrotrade.ui.navigation.scan

import android.graphics.Bitmap
import com.example.retrotrade.ui.navigation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ─── Item category options ──────────────────────────────────────────
enum class ItemCategory(val label: String) {
    TRADING_CARDS("Trading Cards"),
    RETRO_GAMES("Retro Games"),
    VINTAGE_CLOTHING("Vintage Clothing"),
    VINYL_RECORDS("Vinyl Records"),
    COMICS("Comics"),
    TOYS("Toys & Figures"),
    ELECTRONICS("Electronics"),
    OTHER("Other")
}

// ─── Icon char options ──────────────────────────────────────────────
val availableIconChars = listOf(
    "🔥", "🎮", "👖", "🕹️", "⚡", "🧥", "🎵", "📀",
    "🃏", "🎲", "🧸", "📷", "💎", "🏆", "🎸", "📚"
)

// ─── UI State ───────────────────────────────────────────────────────
data class ScanUiState(
    val capturedPhoto: Bitmap? = null,
    val itemName: String = "",
    val selectedCategory: ItemCategory? = null,
    val estimatedValue: String = "",
    val selectedIconChar: String? = null,
    val isCategoryDropdownExpanded: Boolean = false,
    val isSubmitting: Boolean = false,
    val showSuccessMessage: Boolean = false,
    val errorMessage: String? = null
) {
    val isFormValid: Boolean
        get() = capturedPhoto != null &&
                itemName.isNotBlank() &&
                selectedCategory != null &&
                estimatedValue.isNotBlank() &&
                selectedIconChar != null
}

class ScanViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun onPhotoCaptured(bitmap: Bitmap?) {
        _uiState.value = _uiState.value.copy(capturedPhoto = bitmap)
    }

    fun onItemNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(itemName = name)
    }

    fun onCategorySelected(category: ItemCategory) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            isCategoryDropdownExpanded = false
        )
    }

    fun onCategoryDropdownToggle() {
        _uiState.value = _uiState.value.copy(
            isCategoryDropdownExpanded = !_uiState.value.isCategoryDropdownExpanded
        )
    }

    fun onCategoryDropdownDismiss() {
        _uiState.value = _uiState.value.copy(isCategoryDropdownExpanded = false)
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

        _uiState.value = _uiState.value.copy(isSubmitting = true)

        // TODO: send data to backend via repository
        // For now, simulate success
        _uiState.value = _uiState.value.copy(
            isSubmitting = false,
            showSuccessMessage = true
        )
    }

    fun onSuccessDismissed() {
        _uiState.value = ScanUiState()
    }

    fun onClearPhoto() {
        _uiState.value = _uiState.value.copy(capturedPhoto = null)
    }
}
