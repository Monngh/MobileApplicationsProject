package com.example.project.ui.screen.pages.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.project.data.local.TokenManager
import com.example.project.data.remote.RetrofitClient
import com.example.project.data.remote.dto.UserProfile
import com.example.project.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: UserProfile? = null,
    val error: String? = null
)

class ProfileViewModel(
    private val userRepository: UserRepository = RetrofitClient.createUserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var authToken: String? = null

    fun initialize(token: String?) {
        authToken = token
        if (token != null) {
            loadProfile()
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            if (authToken == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "No hay sesión activa"
                )
                return@launch
            }

            val result = userRepository.getUserProfile(authToken!!)

            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    isLoading = false,
                    profile = result.getOrNull()
                )
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Error al cargar perfil"
                )
            }
        }
    }

    fun updateProfile(
        name: String,
        email: String,
        phone: String,
        password: String,  // Siempre requerida
        newPassword: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = userRepository.updateProfile(
                authToken ?: "",
                name,
                email,
                phone,
                password,
                newPassword
            )

            result.fold(
                onSuccess = { updatedProfile ->
                    _uiState.value = _uiState.value.copy(
                        profile = updatedProfile,
                        isLoading = false
                    )
                    onSuccess()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message,
                        isLoading = false
                    )
                }
            )
        }
    }



    fun deleteAccount(password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (authToken == null) return@launch

            val result = userRepository.deleteAccount(authToken!!, password)

            if (result.isSuccess) {
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    error = result.exceptionOrNull()?.message ?: "Error al eliminar cuenta"
                )
            }
        }
    }
}

class ProfileViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
