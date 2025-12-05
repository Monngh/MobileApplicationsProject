package com.example.project.data.repository

import com.example.project.data.remote.api.ApiService
import com.example.project.data.remote.dto.ChatPreview
import com.example.project.data.remote.dto.Message
import com.example.project.data.remote.dto.MessageDTO
import com.example.project.data.remote.dto.SendMessageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageRepository(private val apiService: ApiService) {

    /**
     * Obtener lista de conversaciones (chats)
     * IMPORTANTE: Devuelve List<ChatPreview>
     */
    suspend fun getChats(token: String): Result<List<ChatPreview>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMessages("Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        // apiResponse.data ya es List<ChatPreview>
                        Result.success(apiResponse.data)
                    } else {
                        Result.failure(Exception(apiResponse.message ?: "Error al cargar chats"))
                    }
                } else {
                    Result.failure(Exception("Error HTTP: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    /**
     * Obtener mensajes de un chat específico
     * IMPORTANTE: Devuelve List<Message>
     */
    suspend fun getChatMessages(
        token: String,
        chatId: String
    ): Result<List<Message>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getChatMessages(chatId, "Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        // Mapear MessageDTO a Message
                        val messages = apiResponse.data.map { dto ->
                            Message(
                                id = dto.id,
                                message = dto.message,
                                senderId = dto.sender_id,
                                receiverId = dto.receiver_id,
                                createdAt = dto.created_at,
                                senderName = dto.sender?.name,
                                senderImage = dto.sender?.profile_image,
                                isMine = false // Se asigna en el ViewModel según el userId
                            )
                        }
                        Result.success(messages)
                    } else {
                        Result.failure(Exception(apiResponse.message ?: "Error al cargar mensajes"))
                    }
                } else {
                    Result.failure(Exception("Error HTTP: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    /**
     * Enviar un mensaje
     * IMPORTANTE: Devuelve Message (singular)
     */
    suspend fun sendMessage(
        token: String,
        receiverId: Int,
        message: String,
        propertyId: Int? = null
    ): Result<Message> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.sendMessage(
                    "Bearer $token",
                    SendMessageRequest(
                        receiver_id = receiverId,
                        message = message,
                        property_id = propertyId
                    )
                )

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        val dto = apiResponse.data
                        val messageModel = Message(
                            id = dto.id,
                            message = dto.message,
                            senderId = dto.sender_id,
                            receiverId = dto.receiver_id,
                            createdAt = dto.created_at,
                            senderName = dto.sender?.name,
                            senderImage = dto.sender?.profile_image,
                            isMine = true
                        )
                        Result.success(messageModel)
                    } else {
                        Result.failure(Exception(apiResponse.message ?: "Error al enviar mensaje"))
                    }
                } else {
                    Result.failure(Exception("Error HTTP: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }
}
