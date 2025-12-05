package com.example.project.data.remote

import com.example.project.data.remote.dto.ApiResponse
import com.example.project.data.remote.dto.PropertyResponse
import retrofit2.Response
import retrofit2.http.*

interface FavoriteApiService {

    @GET("favorites")
    suspend fun getFavorites(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<PropertyResponse>>>

    @POST("favorites/{propertyId}")
    suspend fun addToFavorites(
        @Header("Authorization") token: String,
        @Path("propertyId") propertyId: String
    ): Response<ApiResponse<Any>>

    @DELETE("favorites/{propertyId}")
    suspend fun removeFromFavorites(
        @Header("Authorization") token: String,
        @Path("propertyId") propertyId: String
    ): Response<ApiResponse<Any>>
}
