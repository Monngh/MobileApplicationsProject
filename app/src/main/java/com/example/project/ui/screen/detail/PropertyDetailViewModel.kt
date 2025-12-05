package com.example.project.ui.screen.detail

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

sealed class PropertyDetailUiState {
    object Idle : PropertyDetailUiState()
    object Loading : PropertyDetailUiState()
    data class Success(val property: Property) : PropertyDetailUiState()
    data class Error(val message: String) : PropertyDetailUiState()
}

class PropertyDetailViewModel(
    private val context: Context
) : ViewModel() {

    private val tokenManager = TokenManager(context)
    private val propertyRepository = RetrofitClient.createPropertyRepository()
    private val favoriteRepository = RetrofitClient.createFavoriteRepository()

    private val _uiState = MutableStateFlow<PropertyDetailUiState>(PropertyDetailUiState.Idle)
    val uiState: StateFlow<PropertyDetailUiState> = _uiState.asStateFlow()

    private var currentPropertyId: String? = null

    fun loadPropertyDetail(propertyId: String) {
        if (propertyId.isBlank()) {
            _uiState.value = PropertyDetailUiState.Error("ID de propiedad inválido")
            return
        }
        
        currentPropertyId = propertyId
        viewModelScope.launch {
            _uiState.value = PropertyDetailUiState.Loading

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.value = PropertyDetailUiState.Error("No hay token de sesión")
                return@launch
            }

            try {
                val result = propertyRepository.getPropertyDetail(token, propertyId)

                _uiState.value = if (result.isSuccess) {
                    PropertyDetailUiState.Success(result.getOrNull()!!)
                } else {
                    PropertyDetailUiState.Error(result.exceptionOrNull()?.message ?: "Error al cargar propiedad")
                }
            } catch (e: Exception) {
                _uiState.value = PropertyDetailUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is PropertyDetailUiState.Success) return@launch

            val property = currentState.property
            val newFavoriteState = !property.isFavorite

            // Actualizar UI optimistamente
            _uiState.value = PropertyDetailUiState.Success(
                property.copy(isFavorite = newFavoriteState)
            )

            // Sincronizar con backend
            val token = tokenManager.getToken() ?: return@launch
            try {
                if (newFavoriteState) {
                    favoriteRepository.addToFavorites(token, property.id.toString())
                } else {
                    favoriteRepository.removeFromFavorites(token, property.id.toString())
                }
            } catch (e: Exception) {
                // Revertir si falla
                _uiState.value = PropertyDetailUiState.Success(
                    property.copy(isFavorite = !newFavoriteState)
                )
            }
        }
    }
}

class PropertyDetailViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropertyDetailViewModel::class.java)) {
            return PropertyDetailViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
