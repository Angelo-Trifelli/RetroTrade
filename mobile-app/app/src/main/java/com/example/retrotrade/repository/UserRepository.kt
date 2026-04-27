package com.example.retrotrade.repository

import com.example.retrotrade.rest.api.ApiClient
import com.example.retrotrade.rest.api.AuthService
import com.example.retrotrade.rest.model.request.CreateUserRequest
import com.example.retrotrade.rest.model.response.CreateUserResponse

class UserRepository(
    private val authService: AuthService = ApiClient.authService
) {

    suspend fun isUsernameAvailable(username: String): Result<Boolean> {
        return try {
            val response = authService.isUsernameAvailable(username)

            if (!response.isSuccessful) {
                return Result.failure(
                    Exception("HTTP error (${response.code()}): ${response.body()?.error}")
                )
            }

            val body = response.body() ?: return Result.failure(Exception("Empty response"))

            body.error?.let {
                return Result.failure(Exception(it))
            }

            body.available?.let {
                return Result.success(it)
            }

            Result.failure(Exception("Invalid response format"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun createUser(
        fullName: String, username: String, email: String
    ): Result<CreateUserResponse> {
        return try {
            val request = CreateUserRequest(
                fullName = fullName,
                username = username,
                email = email
            )

            val response = authService.createUser(request)

            if (!response.isSuccessful) {
                return Result.failure(
                    Exception("Error:  ${response.body()?.error}")
                )
            }

            val body = response.body() ?: return Result.failure(Exception("Empty response"))
            Result.success(body)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}