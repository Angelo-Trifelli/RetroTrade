package com.example.retrotrade.ui.navigation.collection

import android.graphics.Bitmap
import com.example.retrotrade.rest.model.response.ItemDetailsResponse

data class ItemDetailsUiState(
    val item: ItemDetailsResponse? = null,
    val photoBitmap: Bitmap? = null
)
