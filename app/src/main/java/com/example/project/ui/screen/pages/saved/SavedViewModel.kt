package com.example.project.ui.screen.pages.saved

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.project.data.local.TokenManager
import com.example.project.data.remote.RetrofitClient
import com.example.project.domain.model.Property
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SavedUiState {
    object Idle : SavedUiState()
    object Loading : SavedUiState()
    data class Success(val favorites: List<Property>) : SavedUiState()
    data class Error(val message: String) : SavedUiState()
}

class SavedViewModel(
    private val context: Context
) : ViewModel() {

    private val tokenManager = TokenManager(context)
    private val favoriteRepository = RetrofitClient.createFavoriteRepository()

    private val _uiState = MutableStateFlow<SavedUiState>(SavedUiState.Idle)
    val uiState: StateFlow<SavedUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = SavedUiState.Loading

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.value = SavedUiState.Error("No hay token de sesión")
                return@launch
            }

            try {
                val result = favoriteRepository.getFavorites(token)

                _uiState.value = if (result.isSuccess) {
                    SavedUiState.Success(result.getOrNull() ?: emptyList())
                } else {
                    SavedUiState.Error(result.exceptionOrNull()?.message ?: "Error al cargar favoritos")
                }
            } catch (e: Exception) {
                _uiState.value = SavedUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun removeFromFavorites(propertyId: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is SavedUiState.Success) return@launch

            // Actualizar UI optimistamente
            val updatedFavorites = currentState.favorites.filter { it.id.toString() != propertyId }
            _uiState.value = SavedUiState.Success(updatedFavorites)

            // Sincronizar con backend
            val token = tokenManager.getToken() ?: return@launch
            try {
                favoriteRepository.removeFromFavorites(token, propertyId)
            } catch (e: Exception) {
                // Recargar si falla
                loadFavorites()
            }
        }
    }
}

class SavedViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedViewModel::class.java)) {
            return SavedViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
