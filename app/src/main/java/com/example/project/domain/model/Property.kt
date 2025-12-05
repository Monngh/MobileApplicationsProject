package com.example.project.domain.model

data class Property(
    val id: Int,
    val title: String,
    val description: String,
    val address: String,
    val city: String,
    val state: String,
    val price: Double,
    val bedrooms: Int,
    val bathrooms: Int,
    val squareMeters: Double,
    val propertyType: String?,
    val availableFrom: String?,
    val mainImage: String?,
    val mainImageUrl: String?,
    val images: List<PropertyImage> = emptyList(),
    val isFavorite: Boolean = false,
    val isAvailable: Boolean = true,
    val ownerId: Int,
    val owner: User? = null
) {
    // Propiedad computada para obtener el nombre del propietario
    val ownerName: String
        get() = owner?.name ?: "Propietario"
}

