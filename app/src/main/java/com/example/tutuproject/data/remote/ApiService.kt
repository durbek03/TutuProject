package com.example.tutuproject.data.remote

import com.example.tutuproject.data.modelsDto.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int) : ApiResponse
}