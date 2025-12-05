package com.example.project.data.repository

import com.example.project.data.remote.api.ApiService
import com.example.project.data.remote.dto.ForgotPasswordRequest
import com.example.project.data.remote.dto.LoginRequest
import com.example.project.data.remote.dto.RegisterRequest
import com.example.project.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val apiService: ApiService) {

    /**
     * Login del usuario
     * @return Result<Pair<String, User>> - (token, user)
     */
    suspend fun login(email: String, password: String): Result<Pair<String, User>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    val user = User(
                        id = authResponse.user.id,
                        name = authResponse.user.name,
                        email = authResponse.user.email,
                        phone = authResponse.user.phone,
                        profileImage = authResponse.user.profile_image,
                        accountType = authResponse.user.account_type
                    )
                    Result.success(Pair(authResponse.token, user))
                } else {
                    // Parsear mensaje de error del servidor
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    /**
     * Parsea el mensaje de error del servidor Laravel
     */
    private fun parseErrorMessage(errorBody: String?): String {
        if (errorBody.isNullOrBlank()) return "Credenciales incorrectas"
        return try {
            val json = org.json.JSONObject(errorBody)
            // Intentar obtener mensaje de errores de validación
            if (json.has("errors")) {
                val errors = json.getJSONObject("errors")
                val firstKey = errors.keys().next()
                errors.getJSONArray(firstKey).getString(0)
            } else if (json.has("message")) {
                json.getString("message")
            } else {
                "Credenciales incorrectas"
            }
        } catch (e: Exception) {
            "Credenciales incorrectas"
        }
    }

    /**
     * Registro de nuevo usuario
     */
    suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        accountType: String,
        phone: String? = null
    ): Result<Pair<String, User>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(
                    RegisterRequest(name, email, password, passwordConfirmation, phone, accountType)
                )

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    // MISMO FORMATO QUE LOGIN - SIN success ni data
                    val user = User(
                        id = authResponse.user.id,
                        name = authResponse.user.name,
                        email = authResponse.user.email,
                        phone = authResponse.user.phone,
                        profileImage = authResponse.user.profile_image,
                        accountType = authResponse.user.account_type
                    )
                    Result.success(Pair(authResponse.token, user))
                } else {
                    Result.failure(Exception("Error en la respuesta del servidor"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    /**
     * Cerrar sesión
     */
    suspend fun logout(token: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.logout("Bearer $token")
                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Error al cerrar sesión"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    /**
     * Recuperar contraseña (enviar email)
     */
    suspend fun forgotPassword(email: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.forgotPassword(ForgotPasswordRequest(email))
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    Result.success(apiResponse.message ?: "Correo enviado")
                } else {
                    Result.failure(Exception("Error al enviar correo de recuperación"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    /**
     * Resetear contraseña con email y nueva contraseña
     */
    suspend fun resetPassword(email: String, newPassword: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.resetPassword(
                    com.example.project.data.remote.dto.ResetPasswordRequest(email, newPassword)
                )
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    Result.success(apiResponse.message ?: "Contraseña actualizada correctamente")
                } else {
                    Result.failure(Exception("No existe una cuenta con este correo"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }
}
