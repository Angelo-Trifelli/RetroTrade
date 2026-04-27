package com.example.retrotrade.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _navEvent = MutableSharedFlow<NavEvent>()
    val navEvent = _navEvent.asSharedFlow()

    protected fun navigate(route: String) {
        viewModelScope.launch {
            _navEvent.emit(NavEvent.Navigate(route))
        }
    }

    protected fun popBackStack() {
        viewModelScope.launch {
            _navEvent.emit(NavEvent.PopBackStack)
        }
    }
}