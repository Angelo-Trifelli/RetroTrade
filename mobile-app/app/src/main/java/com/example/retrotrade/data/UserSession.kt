package com.example.retrotrade.data

import com.example.retrotrade.model.User
import com.example.retrotrade.rest.model.response.LoadUserDataResponse

object UserSession {
    var currentUser: User? = null
        private set

    fun set(currentUserData: LoadUserDataResponse) {
        currentUser = User(
            currentUserData.id,
            currentUserData.registeredAt,
            currentUserData.fullName,
            currentUserData.username,
            currentUserData.email
        )
    }

    fun clear() {
        currentUser = null
    }
}