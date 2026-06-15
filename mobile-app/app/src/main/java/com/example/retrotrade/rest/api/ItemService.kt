package com.example.retrotrade.rest.api

import com.example.retrotrade.rest.model.request.CreateItemRequest
import com.example.retrotrade.rest.model.response.ItemDetailsResponse
import com.example.retrotrade.rest.model.response.LoadItemsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemService {

    @GET("/items")
    suspend fun loadItems(
        @Query("userId") userId: String
    ): Response<List<LoadItemsResponse>>

    @POST("/items")
    suspend fun createItem(
        @Body request: CreateItemRequest
    ): Response<Unit>

    @GET("/items/{id}")
    suspend fun getItemDetails(
        @Path("id") id: String
    ): Response<ItemDetailsResponse>

}