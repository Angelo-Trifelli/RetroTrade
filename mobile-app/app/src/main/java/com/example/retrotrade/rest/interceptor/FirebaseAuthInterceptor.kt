package com.example.retrotrade.rest.interceptor

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response

class FirebaseAuthInterceptor(
    private val firebaseAuth: FirebaseAuth
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val token = try {
            runBlocking {
                firebaseAuth.currentUser
                    ?.getIdToken(false)
                    ?.await()
                    ?.token
            }
        } catch (e: Exception) {
            null
        }

        val authenticatedRequest =
            if (!token.isNullOrEmpty()) {
                request.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                request
            }

        return chain.proceed(authenticatedRequest)
    }
}