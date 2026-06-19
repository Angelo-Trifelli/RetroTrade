package com.example.retrotrade.rest.api

import com.example.retrotrade.model.Trade
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TradeService {

    @GET("/trades")
    suspend fun loadTrades(
        @Query("userId") userId: String
    ): Response<List<Trade>>
}