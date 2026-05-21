package com.example.retrotrade.ui.navigation.profile

import androidx.lifecycle.viewModelScope
import com.example.retrotrade.data.UserSession
import com.example.retrotrade.firebaseAuth
import com.example.retrotrade.repository.UserRepository
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
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileViewModel : BaseViewModel() {

    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    //Separate loading/error state so the UI can react without inspecting every field of ProfileUiState
    private val _dataState = MutableStateFlow<GenericUiState>(GenericUiState.Idle)
    val dataState: StateFlow<GenericUiState> = _dataState.asStateFlow()

    /* --------------------------- CONSTRUCTOR --------------------------- */
    init {
        loadProfile()
    }

    /* --------------------------- PUBLIC API --------------------------- */
    fun onLogout() {
        firebaseAuth.signOut()
        UserSession.clear()
        navigate(Screen.AuthGraph.route)
    }

    fun resetDataState() {
        _dataState.value = GenericUiState.Idle
    }


    /* ----------------------- PRIVATE FUNCTIONS ------------------------ */
    private fun loadProfile() {
        viewModelScope.launch {
            _dataState.value = GenericUiState.Loading

            val currentUser = UserSession.currentUser

            try {
                val statsDefer = async(Dispatchers.IO) { userRepository.getStats() }

                val userStatsResponse = statsDefer.await().getOrThrow()

                _uiState.update {
                    it.copy(
                        fullName = currentUser?.fullName ?: "Retro Collector",
                        username = currentUser?.username ?: "collector",
                        email = currentUser?.email ?: "—",
                        memberSince = formatMemberSince(currentUser?.registeredAt),
                        soldItems = userStatsResponse.soldItems,
                        completedTrades = userStatsResponse.completedTrades
                    )
                }

                _dataState.value = GenericUiState.Success
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        fullName = currentUser?.fullName ?: "Unknown",
                        username = currentUser?.username ?: "unknown",
                        email = currentUser?.email ?: "—",
                        memberSince = "Unknown",
                        soldItems = 0,
                        completedTrades = 0
                    )
                }

                _dataState.value = GenericUiState.Error(e.message ?: "Failed to load profile data")
            }
        }
    }

    private fun formatMemberSince(registeredAt: String?): String {
        if (registeredAt == null) return "Unknown"

        return try {
            val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
            val date = inputFormat.parse(registeredAt) ?: return "Unknown"
            outputFormat.format(date)
        } catch (e: Exception) {
            "Unknown"
        }
    }

}
