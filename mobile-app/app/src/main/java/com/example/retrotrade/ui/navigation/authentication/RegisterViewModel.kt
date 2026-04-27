package com.example.retrotrade.ui.navigation.authentication

import androidx.lifecycle.viewModelScope
import com.example.retrotrade.firebaseAuth
import com.example.retrotrade.repository.UserRepository
import com.example.retrotrade.ui.navigation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : BaseViewModel() {

    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(fullName: String, username: String, email: String, password: String) {
        _uiState.value = RegisterUiState.Loading

        viewModelScope.launch {
            val result = userRepository.isUsernameAvailable(username)

            result
                .onFailure { error ->
                    _uiState.value = RegisterUiState.Error(error.message ?: "Registration failed")
                }
                .onSuccess { available ->
                    if (!available) {
                        _uiState.value = RegisterUiState.Error("Username is not available")
                        return@launch
                    }

                    firebaseAuth
                        .createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { authResult ->
                            val user = authResult.user

                            user?.getIdToken(true)
                                ?.addOnSuccessListener { tokenResult ->
                                    val idToken = tokenResult.token

                                    if (idToken != null) {
                                        viewModelScope.launch {
                                            userRepository.createUser(fullName, username, email)
                                                .onSuccess {
                                                    _uiState.value = RegisterUiState.Success
                                                }
                                                .onFailure {
                                                    _uiState.value = RegisterUiState.Error(it.message ?: "Registration failed")
                                                }
                                        }
                                    } else {
                                        _uiState.value = RegisterUiState.Error("Failed to obtain ID token")
                                    }
                                }
                                ?.addOnFailureListener {
                                    _uiState.value = RegisterUiState.Error("Failed to obtain ID token")
                                }
                        }
                        .addOnFailureListener { exception ->
                            _uiState.value = RegisterUiState.Error(exception.message ?: "Registration failed")
                        }
                }
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Idle
    }

    fun onBackToLogin() {
        popBackStack()
    }
}