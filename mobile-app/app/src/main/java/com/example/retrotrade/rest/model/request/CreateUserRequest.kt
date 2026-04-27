package com.example.retrotrade.rest.model.request

data class CreateUserRequest(
    val username: String,
    val fullName: String,
    val email: String
)