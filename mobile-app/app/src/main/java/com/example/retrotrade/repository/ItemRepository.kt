package com.example.retrotrade.repository

import com.example.retrotrade.data.UserSession
import com.example.retrotrade.rest.api.ApiClient
import com.example.retrotrade.rest.api.ItemService
import com.example.retrotrade.rest.model.response.LoadItemsResponse
import com.example.retrotrade.rest.parser.ErrorParser

class ItemRepository(
    private val itemService: ItemService = ApiClient.itemService
) {

    suspend fun loadItems(): Result<List<LoadItemsResponse>> {
        return try {
            val response = itemService.loadItems(UserSession.currentUser?.id.toString())

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