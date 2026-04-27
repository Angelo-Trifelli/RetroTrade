package com.example.retrotrade.ui.navigation.authentication

import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.Screen

class LoginViewModel : BaseViewModel() {

    fun onRegisterClicked() {
        navigate(Screen.Register.route)
    }
}