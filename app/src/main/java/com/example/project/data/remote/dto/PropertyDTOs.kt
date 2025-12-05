package com.example.project.data.remote.dto

import com.google.gson.annotations.SerializedName

// Response para lista de propiedades
data class PropertiesResponse(
    val success: Boolean,
    val message: String?,
    val data: List<PropertyDTO>
)

// Response para eliminar propiedad
data class DeletePropertyResponse(
    val success: Boolean,
    val message: String?
)

// DTO individual de propiedad (COMPLETO)
data class PropertyDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("state")
    val state: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("bedrooms")
    val bedrooms: Int,

    @SerializedName("bathrooms")
    val bathrooms: Int,

    @SerializedName("square_meters")
    val square_meters: Double,

    @SerializedName("property_type")
    val property_type: String,

    @SerializedName("available_from")
    val available_from: String,

    @SerializedName("main_image")
    val main_image: String?,

    @SerializedName("main_image_url")
    val main_image_url: String?,

    @SerializedName("is_available")
    val is_available: Boolean,

    @SerializedName("owner_id")
    val owner_id: Int,

    @SerializedName("is_favorite")
    val is_favorite: Boolean? = false,

    @SerializedName("owner")
    val owner: UserDTO? = null,

    @SerializedName("images")
    val images: List<PropertyImageDTO>? = null
)
