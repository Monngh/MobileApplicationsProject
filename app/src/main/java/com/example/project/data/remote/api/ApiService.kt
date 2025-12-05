package com.example.project.data.remote.api

import com.example.project.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ========================================
    // AUTENTICACIÓN
    // ========================================

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<LoginResponse>

    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Any>>

    @POST("forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<ApiResponse<Any>>

    @POST("reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ApiResponse<Any>>

    // ========================================
    // PROPIEDADES
    // ========================================

    @GET("properties")
    suspend fun getProperties(
        @Header("Authorization") token: String
    ): Response<List<PropertyResponse>>


    @DELETE("properties/{id}")
    suspend fun deleteProperty(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<DeletePropertyResponse>

    // ========================================
    // FAVORITOS
    // ========================================

    @POST("favorites/{propertyId}")
    suspend fun addToFavorites(
        @Path("propertyId") propertyId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Any>>

    @DELETE("favorites/{propertyId}")
    suspend fun removeFromFavorites(
        @Path("propertyId") propertyId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Any>>

    // ========================================
    // MENSAJES / CHAT
    // ========================================

    /**
     * Obtener lista de conversaciones (chats)
     * Retorna List<ChatPreview>
     */
    @GET("messages")
    suspend fun getMessages(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<ChatPreview>>>

    /**
     * Obtener mensajes de un chat específico
     * Retorna List<MessageDTO>
     */
    @GET("messages/{chatId}")
    suspend fun getChatMessages(
        @Path("chatId") chatId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<MessageDTO>>>

    /**
     * Enviar un mensaje
     * Retorna MessageDTO (el mensaje creado)
     */
    @POST("messages")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body request: SendMessageRequest
    ): Response<ApiResponse<MessageDTO>>

    // ========================================
// PERFIL DE USUARIO
// ========================================

    @GET("user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserProfile>>  // ⬅️ CAMBIO: UserProfile en lugar de UserDTO

    @PUT("user/profile")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse<UserProfile>>  // ⬅️ CAMBIO: UserProfile en lugar de UserDTO

    @HTTP(method = "DELETE", path = "user/profile", hasBody = true)
    suspend fun deleteUserAccount(
        @Header("Authorization") token: String,
        @Body request: DeleteAccountRequest
    ): Response<ApiResponse<Map<String, String>>>


    @POST("user/profile/image")
    @Multipart
    suspend fun uploadProfileImage(
        @Header("Authorization") token: String,
        @Part image: okhttp3.MultipartBody.Part
    ): Response<ApiResponse<String>>

    @GET("properties/{id}")
    suspend fun getPropertyDetail(
        @Path("id") propertyId: String,
        @Header("Authorization") token: String
    ): Response<PropertyResponse>

    // ========================================
    // NOTIFICACIONES
    // ========================================

    @GET("notifications")
    suspend fun getNotifications(
        @Header("Authorization") token: String
    ): Response<NotificationsResponse>

    @GET("notifications/unread-count")
    suspend fun getUnreadNotificationCount(
        @Header("Authorization") token: String
    ): Response<UnreadCountResponse>

    @PUT("notifications/{id}/read")
    suspend fun markNotificationAsRead(
        @Path("id") notificationId: Int,
        @Header("Authorization") token: String
    ): Response<NotificationActionResponse>

    @PUT("notifications/mark-all-read")
    suspend fun markAllNotificationsAsRead(
        @Header("Authorization") token: String
    ): Response<NotificationActionResponse>

    @DELETE("notifications/{id}")
    suspend fun deleteNotification(
        @Path("id") notificationId: Int,
        @Header("Authorization") token: String
    ): Response<NotificationActionResponse>

}
