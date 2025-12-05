package com.example.project.data.remote.dto

import com.google.gson.annotations.SerializedName

// DTO para mensajes individuales
data class MessageDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("sender_id")
    val sender_id: Int,

    @SerializedName("receiver_id")
    val receiver_id: Int,

    @SerializedName("created_at")
    val created_at: String,

    @SerializedName("sender")
    val sender: UserDTO? = null,

    @SerializedName("receiver")
    val receiver: UserDTO? = null
)

// Request para enviar mensaje
data class SendMessageRequest(
    val receiver_id: Int,
    val message: String,
    val property_id: Int? = null
)

// Vista previa de chat (conversación) - CORREGIDO
data class ChatPreview(
    @SerializedName("id")
    val id: Int,  // ID del chat

    @SerializedName("chat_id")
    val chatId: String?,  // ID alternativo (string)

    @SerializedName("other_user")
    val otherUser: UserDTO,

    @SerializedName("last_message")
    val lastMessage: String,

    @SerializedName("last_message_time")
    val lastMessageTime: String,

    @SerializedName("unread_count")
    val unreadCount: Int = 0,

    @SerializedName("property")
    val property: PropertyDTO? = null
)

// Modelo de dominio para la UI (mensajes en el chat)
data class Message(
    val id: Int,
    val message: String,
    val senderId: Int,
    val receiverId: Int,
    val createdAt: String,
    val senderName: String? = null,
    val senderImage: String? = null,
    val isMine: Boolean = false
)
