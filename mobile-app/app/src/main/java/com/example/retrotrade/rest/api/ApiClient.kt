package com.example.retrotrade.rest.api

import com.example.retrotrade.firebaseAuth
import com.example.retrotrade.rest.interceptor.FirebaseAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://flask-backend-51170396881.europe-west1.run.app/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = FirebaseAuthInterceptor(firebaseAuth)

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(logging)
        .build()


    val authService: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL + "auth/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }

    val userService: UserService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL + "users/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserService::class.java)
    }

    val itemService: ItemService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItemService::class.java)
    }

    val tradeService: TradeService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TradeService::class.java)
    }
}