package com.example.project.ui.screen.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.data.remote.RetrofitClient
import com.example.project.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository = RetrofitClient.createAuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun resetPassword(email: String, newPassword: String) {
        if (!isValidEmail(email)) {
            _uiState.value = ForgotPasswordUiState.Error("Correo electrónico inválido")
            return
        }

        if (newPassword.length < 8) {
            _uiState.value = ForgotPasswordUiState.Error("La contraseña debe tener al menos 8 caracteres")
            return
        }

        viewModelScope.launch {
            _uiState.value = ForgotPasswordUiState.Loading

            val result = authRepository.resetPassword(email, newPassword)

            if (result.isSuccess) {
                val message = result.getOrNull() ?: "Contraseña actualizada correctamente"
                _uiState.value = ForgotPasswordUiState.Success(message)
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Error al resetear contraseña"
                _uiState.value = ForgotPasswordUiState.Error(errorMessage)
            }
        }
    }

    fun resetState() {
        _uiState.value = ForgotPasswordUiState.Idle
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

sealed class ForgotPasswordUiState {
    object Idle : ForgotPasswordUiState()
    object Loading : ForgotPasswordUiState()
    data class Success(val message: String) : ForgotPasswordUiState()
    data class Error(val message: String) : ForgotPasswordUiState()
}
