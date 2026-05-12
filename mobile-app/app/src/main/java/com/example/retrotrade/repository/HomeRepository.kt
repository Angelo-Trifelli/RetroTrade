package com.example.retrotrade.repository

import com.example.retrotrade.data.UserSession
import com.example.retrotrade.model.RecentItem
import com.example.retrotrade.model.TrendingItem
import com.example.retrotrade.rest.api.ApiClient
import com.example.retrotrade.rest.api.UserService
import com.example.retrotrade.rest.model.response.UserStatsResponse
import com.example.retrotrade.rest.parser.ErrorParser

class HomeRepository(
    private val userService: UserService = ApiClient.userService
) {

    suspend fun getStats(): Result<UserStatsResponse> {
        return try {
            val response = userService.loadStats(UserSession.currentUser?.id.toString())

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

    suspend fun getRecentItems(): Result<List<RecentItem>> {
        return try {
            val response = userService.loadRecentItems(UserSession.currentUser?.id.toString())

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

    fun getTrendingItems(): List<TrendingItem> {
        return listOf(
            TrendingItem("Super Nintendo Console",  "Retro Games",      "$175.00", "RetroMike",    "2.4 km", "🎮"),
            TrendingItem("Blastoise Holo",          "Trading Cards",    "$195.00", "CardTrader99", "1.1 km", "💧"),
            TrendingItem("Vintage Nike Windbreaker","Vintage Clothing", "$88.00",  "ThriftQueen",  "3.7 km", "🧥")
        )
    }
}