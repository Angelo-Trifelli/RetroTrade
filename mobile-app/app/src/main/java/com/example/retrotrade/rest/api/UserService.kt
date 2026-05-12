package com.example.retrotrade.rest.api

import com.example.retrotrade.model.RecentItem
import com.example.retrotrade.rest.model.response.UserStatsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {

    @GET("{id}/stats")
    suspend fun loadStats(
        @Path("id") userId: String
    ): Response<UserStatsResponse>

    @GET("{id}/history/recentItems")
    suspend fun loadRecentItems(
        @Path("id") userId: String
    ): Response<List<RecentItem>>

}