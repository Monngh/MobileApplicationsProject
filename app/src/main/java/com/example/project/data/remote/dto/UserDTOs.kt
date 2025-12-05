package com.example.project.data.remote.dto

import com.google.gson.annotations.SerializedName

// ========================================
// USER DTO (Para Login, Register y referencias)
// ========================================
data class UserDTO(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String? = null,
    @SerializedName("profile_image")
    val profile_image: String? = null,
    @SerializedName("account_type")
    val account_type: String? = null
)

// ========================================
// PERFIL DE USUARIO COMPLETO (Para getUserProfile)
// ========================================
data class UserProfile(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName("account_type")
    val accountType: String,
    @SerializedName("profile_image")
    val profileImage: String? = null,
    val phone: String? = null,
    val bio: String? = null,
    @SerializedName("properties_count")
    val propertiesCount: Int = 0,
    @SerializedName("favorites_count")
    val favoritesCount: Int = 0,
    @SerializedName("created_at")
    val createdAt: String? = null
)

// ========================================
// ACTUALIZAR PERFIL
// ========================================
data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val phone: String?,
    val password: String,  // Siempre requerida
    val new_password: String? = null,
    val new_password_confirmation: String? = null
)


// ========================================
// ELIMINAR CUENTA
// ========================================
data class DeleteAccountRequest(                       // ← NUEVO DTO
    val password: String
)

// ========================================
// RESPUESTA DE SUBIDA DE IMAGEN
// ========================================
data class ImageUploadResponse(
    val message: String,
    val url: String,
    @SerializedName("image_path")
    val imagePath: String
)
