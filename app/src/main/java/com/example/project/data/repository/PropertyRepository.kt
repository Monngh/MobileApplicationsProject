package com.example.project.data.repository

import com.example.project.data.remote.api.ApiService
import com.example.project.domain.model.Property
import com.example.project.domain.model.PropertyImage
import com.example.project.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PropertyRepository(private val apiService: ApiService) {

    /**
     * Obtener todas las propiedades
     */
    suspend fun getProperties(token: String): Result<List<Property>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProperties("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    val propertiesResponse = response.body()!!  // Ya es List<PropertyResponse>
                    // Mapear PropertyResponse a Property
                    val properties = propertiesResponse.map { dto ->
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
                            isFavorite = dto.is_favorite ?: false,
                            isAvailable = dto.is_available,
                            ownerId = dto.owner_id,
                            owner = dto.owner?.let { ownerDto ->
                                User(
                                    id = ownerDto.id,
                                    name = ownerDto.name,
                                    email = ownerDto.email,
                                    phone = ownerDto.phone,
                                    profileImage = ownerDto.profile_image,
                                    accountType = ownerDto.account_type
                                )
                            }
                        )
                    }
                    Result.success(properties)
                } else {
                    Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }


    /**
     * Obtener detalle de una propiedad específica
     */
    suspend fun getPropertyDetail(token: String, propertyId: String): Result<Property> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPropertyDetail(propertyId, "Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    val dto = response.body()!!
                    // Mapear PropertyResponse a Property
                    val property = Property(
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
                        images = dto.images?.map { imgDto ->
                            PropertyImage(
                                id = imgDto.id,
                                propertyId = imgDto.property_id,
                                imageUrl = imgDto.image_url,
                                isPrimary = imgDto.is_primary,
                                order = imgDto.order
                            )
                        } ?: emptyList(),
                        isFavorite = dto.is_favorite ?: false,
                        isAvailable = dto.is_available,
                        ownerId = dto.owner_id,
                        owner = dto.owner?.let { ownerDto ->
                            User(
                                id = ownerDto.id,
                                name = ownerDto.name,
                                email = ownerDto.email,
                                phone = ownerDto.phone,
                                profileImage = ownerDto.profile_image,
                                accountType = ownerDto.account_type
                            )
                        }
                    )
                    Result.success(property)
                } else {
                    Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }


    /**
     * Eliminar una propiedad
     */
    suspend fun deleteProperty(token: String, propertyId: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteProperty(propertyId, "Bearer $token")

                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Error al eliminar propiedad: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
