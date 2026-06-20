package com.example.retrotrade.ui.navigation.item

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.retrotrade.repository.ItemRepository
import com.example.retrotrade.repository.TradeRepository
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

class ItemDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val itemRepository = ItemRepository()
    private val tradeRepository = TradeRepository()

    private val _uiState = MutableStateFlow(ItemDetailsUiState())
    val uiState: StateFlow<ItemDetailsUiState> = _uiState.asStateFlow()

    //Separate loading/error state so the UI can react without inspecting every field of ItemDetailsUiState
    private val _dataState = MutableStateFlow<GenericUiState>(GenericUiState.Loading)
    val dataState: StateFlow<GenericUiState> = _dataState.asStateFlow()

    /* --------------------------- CONSTRUCTOR --------------------------- */
    init {
        val itemId: String? = savedStateHandle.get<String>("itemId")
        val source: String? = savedStateHandle.get<String>("source")

        if (itemId != null) {
            loadItemDetails(itemId, source)
        } else {
            _dataState.value = GenericUiState.Error("Item ID is missing")
        }
    }

    /* --------------------------- PUBLIC API --------------------------- */
    fun onGoBack() {
        popBackStack()
    }

    fun onSubmitOffer(amount: String, message: String?) {
        viewModelScope.launch {
            tradeRepository.createTrade(
                _uiState.value.item?.id!!,
                amount,
                message
            ).onSuccess {
                _dataState.value = GenericUiState.Success
            }.onFailure {
                _dataState.value = GenericUiState.Error(it.message ?: "Failed to create trade")
            }
        }
    }

    fun resetDataState() {
        _dataState.value = GenericUiState.Idle
    }

    /* ----------------------- PRIVATE FUNCTIONS ------------------------ */
    private fun loadItemDetails(itemId: String, source: String?) {
        viewModelScope.launch {
            _dataState.value = GenericUiState.Loading

            try {
                val itemDefer = async(Dispatchers.IO) { itemRepository.loadItemDetails(itemId) }
                val registerView = if (source == Screen.Map.route) {
                    async(Dispatchers.IO) { itemRepository.registerItemView(itemId) }
                } else null

                val itemResponse = itemDefer.await().getOrThrow()
                registerView?.await()?.getOrNull()

                val bitmap = decodeBase64ToBitmap(itemResponse.photo)

                _uiState.update {
                    it.copy(
                        item = itemResponse,
                        photoBitmap = bitmap,
                        isFromMap = source != null && source == Screen.Map.route
                    )
                }

                _dataState.value = GenericUiState.Success
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        item = null,
                        photoBitmap = null,
                        isFromMap = false
                    )
                }

                _dataState.value = GenericUiState.Error(e.message ?: "Failed to load item details")
            }
        }
    }

    private fun decodeBase64ToBitmap(base64Str: String?): Bitmap? {
        if (base64Str.isNullOrEmpty()) return null
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }
}
