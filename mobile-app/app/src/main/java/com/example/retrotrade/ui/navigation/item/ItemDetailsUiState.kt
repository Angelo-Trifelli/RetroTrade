package com.example.retrotrade.ui.navigation.item

import android.graphics.Bitmap
import com.example.retrotrade.rest.model.response.ItemDetailsResponse

data class ItemDetailsUiState(
    val item: ItemDetailsResponse? = null,
    val photoBitmap: Bitmap? = null,
    val isFromMap: Boolean = false
)
