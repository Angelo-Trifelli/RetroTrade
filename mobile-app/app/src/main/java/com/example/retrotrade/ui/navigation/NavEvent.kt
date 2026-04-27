package com.example.retrotrade.ui.navigation

sealed interface NavEvent {

    data class Navigate(val route: String) : NavEvent

    data object PopBackStack : NavEvent
}