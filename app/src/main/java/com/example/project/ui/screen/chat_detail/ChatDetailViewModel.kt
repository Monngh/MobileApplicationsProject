package com.example.project.ui.screen.chat_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.data.remote.RetrofitClient
import com.example.project.data.remote.dto.Message
import com.example.project.data.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatDetailViewModel(
    private val messageRepository: MessageRepository = RetrofitClient.createMessageRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatDetailUiState())
    val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

    private var authToken: String? = null
    private var chatId: String? = null

    fun initialize(token: String?, chatId: String) {
        this.authToken = token
        this.chatId = chatId

        if (token != null) {
            loadMessages()
        } else {
            loadMockMessages()
        }
    }

    fun loadMessages() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            if (authToken != null && chatId != null) {
                val result = messageRepository.getChatMessages(authToken!!, chatId!!)

                if (result.isSuccess) {
                    val messages = result.getOrNull() ?: emptyList()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        messages = messages
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message
                    )
                }
            } else {
                loadMockMessages()
            }
        }
    }

    fun sendMessage(receiverId: Int, messageText: String) {
        if (messageText.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true)

            if (authToken != null) {
                val result = messageRepository.sendMessage(authToken!!, receiverId, messageText)

                if (result.isSuccess) {
                    val newMessage = result.getOrNull()!!
                    val updatedMessages = _uiState.value.messages + newMessage
                    _uiState.value = _uiState.value.copy(
                        isSending = false,
                        messages = updatedMessages
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isSending = false,
                        error = "Error al enviar mensaje"
                    )
                }
            }
        }
    }

    private fun loadMockMessages() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            kotlinx.coroutines.delay(1000)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                messages = emptyList()
            )
        }
    }
}

data class ChatDetailUiState(
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val error: String? = null,
    val messages: List<Message> = emptyList()
)
