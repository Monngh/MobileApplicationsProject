package com.example.project.data.remote.dto

import com.google.gson.annotations.SerializedName

// ========== REQUEST ===========

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String,
    val phone: String? = null,
    val account_type: String  // <- Cambiar a account_type (snake_case)
)

data class ForgotPasswordRequest(
    val email: String
)

data class ResetPasswordRequest(
    val email: String,
    @SerializedName("new_password")
    val newPassword: String
)

// ========== RESPONSE ===========

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: UserDTO
)
