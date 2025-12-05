package com.example.project.ui.screen.pages.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.data.remote.RetrofitClient
import com.example.project.data.remote.dto.ChatPreview
import com.example.project.data.repository.MessageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MessagesViewModel(
    private val messageRepository: MessageRepository = RetrofitClient.createMessageRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()

    private var authToken: String? = null

    fun initialize(token: String?) {
        authToken = token
        if (token != null) {
            loadChats()
        } else {
            loadMockChats()
        }
    }

    fun loadChats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            if (authToken != null) {
                val result = messageRepository.getChats(authToken!!)

                if (result.isSuccess) {
                    val chats = result.getOrNull() ?: emptyList()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        chats = chats
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message
                    )
                }
            } else {
                loadMockChats()
            }
        }
    }

    fun onSearchTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(searchText = text)
        filterChats()
    }

    private fun filterChats() {
        // TODO: Implementar búsqueda local
        val filtered = _uiState.value.chats.filter {
            it.otherUser.name.contains(_uiState.value.searchText, ignoreCase = true)
        }
        _uiState.value = _uiState.value.copy(chats = filtered)
    }

    private fun loadMockChats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(1000)

            // Mock data vacío - se llenará con datos reales
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                chats = emptyList()
            )
        }
    }
}

data class MessagesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val chats: List<ChatPreview> = emptyList(),
    val searchText: String = ""
)
