package com.example.project.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.data.remote.RetrofitClient
import com.example.project.data.repository.AuthRepository
import com.example.project.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository = RetrofitClient.createAuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Realizar login con el backend real
     */
    fun login(
        email: String,
        password: String,
        onSuccess: (String, User) -> Unit
    ) {
        // Validaciones locales
        if (!isValidEmail(email)) {
            _uiState.value = LoginUiState.Error("Correo electrónico inválido")
            return
        }

        if (password.length < 8) {
            _uiState.value = LoginUiState.Error("La contraseña debe tener al menos 8 caracteres")
            return
        }

        // Llamada a la API
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val result = authRepository.login(email, password)

            if (result.isSuccess) {
                val (token, user) = result.getOrNull()!!
                _uiState.value = LoginUiState.Success
                onSuccess(token, user)
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                _uiState.value = LoginUiState.Error(errorMessage)
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
