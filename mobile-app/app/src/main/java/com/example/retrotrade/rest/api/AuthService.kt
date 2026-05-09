package com.example.retrotrade.rest.api

import com.example.retrotrade.rest.model.request.CreateUserRequest
import com.example.retrotrade.rest.model.response.CreateUserResponse
import com.example.retrotrade.rest.model.response.LoadUserDataResponse
import com.example.retrotrade.rest.model.response.UsernameAvailableResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @GET("usernameAvailable")
    suspend fun isUsernameAvailable(
        @Query("username") username: String
    ): Response<UsernameAvailableResponse>

    @POST("createUser")
    suspend fun createUser(
        @Body request: CreateUserRequest
    ): Response<CreateUserResponse>

    @GET("loadLoggedUserData")
    suspend fun loadLoggedUserData(): Response<LoadUserDataResponse>
}