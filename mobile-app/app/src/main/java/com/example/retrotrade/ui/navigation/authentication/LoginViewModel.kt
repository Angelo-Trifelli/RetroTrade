package com.example.retrotrade.ui.navigation.authentication

import androidx.lifecycle.viewModelScope
import com.example.retrotrade.data.UserSession
import com.example.retrotrade.firebaseAuth
import com.example.retrotrade.repository.UserRepository
import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {

    private val userRepository = UserRepository()
    private var sessionChecked = false

    private val _uiState = MutableStateFlow<GenericUiState>(GenericUiState.Idle)
    val uiState: StateFlow<GenericUiState> = _uiState.asStateFlow()


    fun checkExistingSession() {
        if (sessionChecked) return

        sessionChecked = true
        val currentUser = firebaseAuth.currentUser

        if (currentUser == null) return

        _uiState.value = GenericUiState.Loading

        currentUser.getIdToken(true)
            .addOnSuccessListener {
                try {
                    loadUserData(true)
                } catch (_: Exception) {
                    firebaseAuth.signOut()
                    _uiState.value = GenericUiState.Idle
                }
            }
            .addOnFailureListener {
                firebaseAuth.signOut()
                _uiState.value = GenericUiState.Idle
            }
    }

    fun onRegisterClicked() {
        navigate(Screen.Register.route)
    }

    fun onForgotPasswordClicked() {
        navigate(Screen.ForgotPassword.route)
    }

    fun onLogin(email: String, password: String) {
        _uiState.value = GenericUiState.Loading

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    _uiState.value = GenericUiState.Error(task.exception?.message ?: "Login failed")
                    return@addOnCompleteListener
                }

                loadUserData(false)
            }
    }

    fun onLoginCompleted() {
        navigate(Screen.MainGraph.route)
    }

    fun resetState() {
        _uiState.value = GenericUiState.Idle
    }

    private fun loadUserData(signOutOnFailure: Boolean) {
        viewModelScope.launch {
            try {
                delay(2000)
                val result = userRepository.getCurrentUser()

                if (result.isFailure) {
                    if (signOutOnFailure) {
                        firebaseAuth.signOut()
                        _uiState.value = GenericUiState.Idle
                    } else {
                        _uiState.value = GenericUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load user data")
                    }
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