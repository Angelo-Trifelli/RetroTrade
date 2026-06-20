package com.example.retrotrade.repository

import android.graphics.Bitmap
import com.example.retrotrade.data.UserSession
import com.example.retrotrade.model.ItemCategory
import com.example.retrotrade.rest.api.ApiClient
import com.example.retrotrade.rest.api.ItemService
import com.example.retrotrade.rest.model.request.CreateItemRequest
import com.example.retrotrade.rest.model.response.ItemDetailsResponse
import com.example.retrotrade.rest.model.response.LoadItemsResponse
import com.example.retrotrade.rest.parser.ErrorParser

class ItemRepository(
    private val itemService: ItemService = ApiClient.itemService
) {

    suspend fun loadItems(): Result<List<LoadItemsResponse>> {
        return try {
            if (UserSession.currentUser == null || UserSession.currentUser?.id == null) {
                return Result.failure(Exception("User not logged in"))
            }

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

    suspend fun loadMapItems(): Result<List<LoadItemsResponse>> {
        return try {
            val response = itemService.loadItems(null)

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

    suspend fun createItem(photo: String, name: String, category: ItemCategory, estimatedValue: String,
                           iconChar: String, latitude: Double, longitude: Double): Result<Unit> {
        return try {
            val request = CreateItemRequest(
                photo = photo,
                name = name,
                category = category,
                estimatedValue = estimatedValue,
                iconChar = iconChar,
                latitude = latitude,
                longitude = longitude
            )

            val response = itemService.createItem(request)

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

    suspend fun loadItemDetails(itemId: String): Result<ItemDetailsResponse> {
        return try {
            val response = itemService.getItemDetails(itemId)

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

    suspend fun registerItemView(itemId: String): Result<Unit> {
        return try {
            val response = itemService.addItemView(itemId)

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
}