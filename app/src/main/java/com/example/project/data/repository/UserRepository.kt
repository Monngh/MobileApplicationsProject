package com.example.project.data.repository

import com.example.project.data.remote.api.ApiService
import com.example.project.data.remote.dto.DeleteAccountRequest
import com.example.project.data.remote.dto.UpdateProfileRequest
import com.example.project.data.remote.dto.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val apiService: ApiService) {

    /**
     * Obtener perfil del usuario
     */
    suspend fun getUserProfile(token: String): Result<UserProfile> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserProfile("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        Result.success(apiResponse.data)
                    } else {
                        Result.failure(Exception(apiResponse.message ?: "Error"))
                    }
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Actualizar perfil del usuario
     * Requiere password para confirmar cambios
     */
    suspend fun updateProfile(
        token: String,
        name: String,
        email: String,
        phone: String,
        password: String,
        newPassword: String? = null
    ): Result<UserProfile> {
        return withContext(Dispatchers.IO) {
            try {
                val request = UpdateProfileRequest(
                    name = name,
                    email = email,
                    phone = phone,
                    password = password,
                    new_password = newPassword,
                    new_password_confirmation = newPassword
                )
                val response = apiService.updateUserProfile("Bearer $token", request)

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        Result.success(apiResponse.data)
                    } else {
                        Result.failure(Exception(apiResponse.message ?: "Error al actualizar"))
                    }
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Eliminar cuenta del usuario
     */
    suspend fun deleteAccount(token: String, password: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val request = DeleteAccountRequest(password)
                val response = apiService.deleteUserAccount("Bearer $token", request)

                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Parsea el mensaje de error del servidor Laravel
     */
    private fun parseErrorMessage(errorBody: String?): String {
        if (errorBody.isNullOrBlank()) return "Error en la operación"
        return try {
            val json = org.json.JSONObject(errorBody)
            if (json.has("errors")) {
                val errors = json.getJSONObject("errors")
                val firstKey = errors.keys().next()
                errors.getJSONArray(firstKey).getString(0)
            } else if (json.has("message")) {
                json.getString("message")
            } else {
                "Error en la operación"
            }
        } catch (e: Exception) {
            "Error en la operación"
        }
    }
}

