package com.example.retrotrade.ui.navigation.authentication

import com.example.retrotrade.firebaseAuth
import com.example.retrotrade.ui.navigation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ForgotPasswordViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    fun sendPasswordResetEmail(email: String) {
        _uiState.value = ForgotPasswordUiState.Loading

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                _uiState.value = ForgotPasswordUiState.Success
            }
            .addOnFailureListener { exception ->
                _uiState.value = ForgotPasswordUiState.Error(
                    exception.message ?: "Failed to send reset email"
                )
            }
    }

    fun resetState() {
        _uiState.value = ForgotPasswordUiState.Idle
    }

    fun onBackToLogin() {
        popBackStack()
    }
}
