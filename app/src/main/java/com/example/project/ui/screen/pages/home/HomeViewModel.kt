package com.example.project.ui.screen.pages.home

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

sealed class HomeUiState {
    object Idle : HomeUiState()
    object Loading : HomeUiState()
    data class Success(val properties: List<Property>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val propertyRepository = RetrofitClient.createPropertyRepository()
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadProperties()
    }

    fun loadProperties() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            val token = tokenManager.getToken()
            if (token == null) {
                _uiState.value = HomeUiState.Error("No hay token de sesión")
                return@launch
            }

            val result = propertyRepository.getProperties(token)

            _uiState.value = if (result.isSuccess) {
                HomeUiState.Success(result.getOrNull()!!)
            } else {
                HomeUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }
}

class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val tokenManager = TokenManager(context)
            return HomeViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
