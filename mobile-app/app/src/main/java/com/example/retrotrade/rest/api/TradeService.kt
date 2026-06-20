package com.example.retrotrade.rest.api

import com.example.retrotrade.model.ChatMessage
import com.example.retrotrade.model.Trade
import com.example.retrotrade.rest.model.request.CreateTradeMessageRequest
import com.example.retrotrade.rest.model.request.CreateTradeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TradeService {

    @GET("/trades")
    suspend fun loadTrades(
        @Query("userId") userId: String
    ): Response<List<Trade>>

    @GET("/trades/{id}")
    suspend fun loadTrade(
        @Path("id") id: String
    ): Response<Trade>

    @GET("/trades/{id}/messages")
    suspend fun loadTradeMessages(
        @Path("id") id: String
    ): Response<List<ChatMessage>>

    @POST("/trades")
    suspend fun createTrade(
        @Body request: CreateTradeRequest
    ): Response<Unit>

    @POST("/trades/{id}/messages")
    suspend fun createTradeMessage(
        @Path("id") id: String,
        @Body request: CreateTradeMessageRequest
    ): Response<ChatMessage>

    @POST("/trades/{id}/reject")
    suspend fun rejectTrade(
        @Path("id") id: String
    ): Response<Unit>

    @POST("/trades/{id}/accept")
    suspend fun acceptTrade(
        @Path("id") id: String
    ): Response<Unit>

    @POST("/trades/{id}/complete")
    suspend fun completeTrade(
        @Path("id") id: String
    ): Response<Unit>
}