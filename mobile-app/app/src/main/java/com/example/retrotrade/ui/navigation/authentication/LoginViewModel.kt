package com.example.retrotrade.ui.navigation.authentication

import androidx.lifecycle.viewModelScope
import com.example.retrotrade.data.UserSession
import com.example.retrotrade.firebaseAuth
import com.example.retrotrade.repository.UserRepository
import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {

    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow<GenericUiState>(GenericUiState.Idle)
    val uiState: StateFlow<GenericUiState> = _uiState

    fun onRegisterClicked() {
        navigate(Screen.Register.route)
    }

    fun onLogin(email: String, password: String) {
        _uiState.value = GenericUiState.Loading

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    _uiState.value = GenericUiState.Error(task.exception?.message ?: "Login failed")
                    return@addOnCompleteListener
                }

                loadUserData()
            }
    }

    fun onLoginCompleted() {
        navigate(Screen.MainGraph.route)
    }

    fun resetState() {
        _uiState.value = GenericUiState.Idle
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val result = userRepository.getCurrentUser()

                if (result.isFailure) {
                    _uiState.value = GenericUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load user data")
                } else {
                    UserSession.set(result.getOrThrow())
                    _uiState.value = GenericUiState.Success
                }
            } catch (e: Exception) {
                _uiState.value = GenericUiState.Error(e.message ?: "Failed to load user data")
            }
        }
    }
}