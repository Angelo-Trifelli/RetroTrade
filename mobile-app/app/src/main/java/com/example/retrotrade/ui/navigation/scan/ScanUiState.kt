package com.example.retrotrade.ui.navigation.scan

import android.graphics.Bitmap
import android.util.Base64
import com.example.retrotrade.model.ItemCategory
import java.io.ByteArrayOutputStream

data class ScanUiState(
    val capturedPhoto: Bitmap? = null,
    val itemName: String = "",
    val selectedCategory: ItemCategory? = null,
    val estimatedValue: String = "",
    val selectedIconChar: String? = null,
    val showSuccessMessage: Boolean = false,
    val errorMessage: String? = null
) {
    val isFormValid: Boolean
        get() = capturedPhoto != null &&
                itemName.isNotBlank() &&
                selectedCategory != null &&
                estimatedValue.isNotBlank() &&
                selectedIconChar != null

    val base64Photo: String
        get() {
            if (capturedPhoto == null) return ""
            val stream = ByteArrayOutputStream()
            capturedPhoto.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            val bytes = stream.toByteArray()
            return Base64.encodeToString(bytes, Base64.NO_WRAP)
        }
}