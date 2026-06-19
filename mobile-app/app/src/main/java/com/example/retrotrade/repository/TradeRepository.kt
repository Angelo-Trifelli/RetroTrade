package com.example.retrotrade.repository

import com.example.retrotrade.data.UserSession
import com.example.retrotrade.model.Trade
import com.example.retrotrade.rest.api.ApiClient
import com.example.retrotrade.rest.api.TradeService
import com.example.retrotrade.rest.parser.ErrorParser

class TradeRepository(
    private val tradeService: TradeService = ApiClient.tradeService
) {

    suspend fun loadTrades(): Result<List<Trade>> {
        return try {
            val response = tradeService.loadTrades(UserSession.currentUser?.id.toString())

            if (!response.isSuccessful) {
                return Result.failure(
                    Exception(ErrorParser.parseError(response.errorBody()) ?: "Unknown error")
                )
            }

            val body = response.body() ?: return Result.failure(Exception("Empty response"))
            Result.success(body)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}