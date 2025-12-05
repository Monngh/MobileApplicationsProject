package com.example.project.data.repository

import com.example.project.data.remote.FavoriteApiService
import com.example.project.domain.model.Property
import com.example.project.domain.model.User

class FavoriteRepository(
    private val favoriteApiService: FavoriteApiService
) {

    suspend fun getFavorites(token: String): Result<List<Property>> {
        return try {
            val response = favoriteApiService.getFavorites("Bearer $token")

            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    val properties = apiResponse.data.map { dto ->
                        Property(
                            id = dto.id,
                            title = dto.title,
                            description = dto.description,
                            address = dto.address,
                            city = dto.city,
                            state = dto.state,
                            price = dto.price,
                            bedrooms = dto.bedrooms,
                            bathrooms = dto.bathrooms,
                            squareMeters = dto.square_meters,
                            propertyType = dto.property_type,
                            availableFrom = dto.available_from,
                            mainImage = dto.main_image,
                            mainImageUrl = dto.main_image_url,
                            isFavorite = true,
                            isAvailable = dto.is_available,
                            ownerId = dto.owner_id,
                            owner = dto.owner?.let {
                                User(
                                    id = it.id,
                                    name = it.name,
                                    email = it.email,
                                    phone = it.phone,
                                    profileImage = it.profile_image
                                )
                            }
                        )
                    }
                    Result.success(properties)
                } else {
                    Result.failure(Exception(apiResponse?.message ?: "Error desconocido"))
                }
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToFavorites(token: String, propertyId: String): Result<Boolean> {
        return try {
            val response = favoriteApiService.addToFavorites("Bearer $token", propertyId)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error al agregar a favoritos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFromFavorites(token: String, propertyId: String): Result<Boolean> {
        return try {
            val response = favoriteApiService.removeFromFavorites("Bearer $token", propertyId)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error al quitar de favoritos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
