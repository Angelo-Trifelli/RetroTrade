package com.example.retrotrade.repository

import com.example.retrotrade.data.UserSession
import com.example.retrotrade.model.ChatMessage
import com.example.retrotrade.model.Trade
import com.example.retrotrade.rest.api.ApiClient
import com.example.retrotrade.rest.api.TradeService
import com.example.retrotrade.rest.model.request.CreateTradeMessageRequest
import com.example.retrotrade.rest.model.request.CreateTradeRequest
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

    suspend fun loadTrade(tradeId: String): Result<Trade> {
        return try {
            val response = tradeService.loadTrade(tradeId)

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

    suspend fun loadTradeMessages(tradeId: String): Result<List<ChatMessage>> {
        return try {
            val response = tradeService.loadTradeMessages(tradeId)

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


    suspend fun createTrade(itemId: String, amount: String, message: String?): Result<Unit> {
        return try {
            val request = CreateTradeRequest(
                itemId = itemId,
                amount = amount,
                message = message
            )

            val response = tradeService.createTrade(request)

            if (!response.isSuccessful) {
                return Result.failure(
                    Exception(ErrorParser.parseError(response.errorBody()) ?: "Unknown error")
                )
            }

            Result.success(response.body() ?: Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTradeMessage(tradeId: String, message: String): Result<ChatMessage?> {
        return try {
            val request = CreateTradeMessageRequest(
                text = message
            )

            val response = tradeService.createTradeMessage(tradeId, request)

            if (!response.isSuccessful) {
                return Result.failure(
                    Exception(ErrorParser.parseError(response.errorBody()) ?: "Unknown error")
                )
            }

            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun rejectTrade(tradeId: String): Result<Unit> {
        return try {
            val response = tradeService.rejectTrade(tradeId)

            if (!response.isSuccessful) {
                return Result.failure(
                    Exception(ErrorParser.parseError(response.errorBody()) ?: "Unknown error")
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun acceptTrade(tradeId: String): Result<Unit> {
        return try {
            val response = tradeService.acceptTrade(tradeId)

            if (!response.isSuccessful) {
                return Result.failure(
                    Exception(ErrorParser.parseError(response.errorBody()) ?: "Unknown error")
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun completeTrade(tradeId: String): Result<Unit> {
        return try {
            val response = tradeService.completeTrade(tradeId)

            if (!response.isSuccessful) {
                return Result.failure(
                    Exception(ErrorParser.parseError(response.errorBody()) ?: "Unknown error")
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}