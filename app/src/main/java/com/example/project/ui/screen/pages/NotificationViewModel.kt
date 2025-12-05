package com.example.project.ui.screen.main.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            kotlinx.coroutines.delay(1000)

            // Mock notifications
            val mockNotifications = listOf(
                NotificationItem("1", "Nueva propiedad disponible", "Hay una nueva propiedad cerca de ti", "Hace 2 horas", false),
                NotificationItem("2", "Mensaje nuevo", "Juan te envió un mensaje", "Hace 3 horas", true),
                NotificationItem("3", "Favorito actualizado", "Una de tus propiedades favoritas bajó de precio", "Ayer", false)
            )

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                notifications = mockNotifications
            )
        }
    }

    fun markAsRead(notificationId: String) {
        val updated = _uiState.value.notifications.map {
            if (it.id == notificationId) it.copy(isRead = true) else it
        }
        _uiState.value = _uiState.value.copy(notifications = updated)
    }
}

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<NotificationItem> = emptyList()
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean
)
