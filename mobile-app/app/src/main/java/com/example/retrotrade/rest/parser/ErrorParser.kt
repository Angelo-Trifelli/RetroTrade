package com.example.retrotrade.rest.parser

import com.example.retrotrade.rest.model.response.ErrorResponse
import com.google.gson.Gson
import okhttp3.ResponseBody

object ErrorParser {

    fun parseError(errorBody: ResponseBody?, ): String? {
        return try {
            errorBody?.string()?.let {
                Gson().fromJson(it, ErrorResponse::class.java)?.error
            }
        } catch (e: Exception) {
            null
        }
    }
}