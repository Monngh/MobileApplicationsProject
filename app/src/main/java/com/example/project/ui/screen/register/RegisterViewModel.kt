package com.example.project.ui.screen.register

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.data.remote.RetrofitClient
import com.example.project.data.repository.AuthRepository
import com.example.project.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository = RetrofitClient.createAuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    /**
     * Realizar registro con el backend real
     */
    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        accountType: String,
        onSuccess: (String, User) -> Unit
    ) {
        // Validaciones locales
        when {
            name.isBlank() -> {
                _uiState.value = RegisterUiState.Error("El nombre es requerido")
                return
            }
            name.length < 3 -> {
                _uiState.value = RegisterUiState.Error("El nombre debe tener al menos 3 caracteres")
                return
            }
            !isValidEmail(email) -> {
                _uiState.value = RegisterUiState.Error("Correo electrónico inválido")
                return
            }
            password.length < 8 -> {
                _uiState.value = RegisterUiState.Error("La contraseña debe tener al menos 8 caracteres")
                return
            }
            password != confirmPassword -> {
                _uiState.value = RegisterUiState.Error("Las contraseñas no coinciden")
                return
            }
            accountType.isBlank() -> {
                _uiState.value = RegisterUiState.Error("Selecciona un tipo de cuenta")
                return
            }
        }

        // Llamada a la API
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading

            val result = authRepository.register(
                name = name,
                email = email,
                password = password,
                passwordConfirmation = confirmPassword,
                accountType = accountType
            )

            if (result.isSuccess) {
                val (token, user) = result.getOrNull()!!
                _uiState.value = RegisterUiState.Success
                onSuccess(token, user)
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Error al crear cuenta"
                _uiState.value = RegisterUiState.Error(errorMessage)
            }
        }
    }

    /**
     * Obtener fortaleza de la contraseña
     */
    fun getPasswordStrength(password: String): PasswordStrength {
        return when {
            password.length < 6 -> PasswordStrength.WEAK
            password.length < 8 -> PasswordStrength.MEDIUM
            password.length >= 8 &&
                    password.any { it.isUpperCase() } &&
                    password.any { it.isLowerCase() } &&
                    password.any { it.isDigit() } -> PasswordStrength.STRONG
            else -> PasswordStrength.MEDIUM
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Idle
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

enum class PasswordStrength(val label: String, val color: Color) {
    WEAK("Débil", Color.Red),
    MEDIUM("Media", Color(0xFFFFA500)),
    STRONG("Fuerte", Color(0xFF4CAF50))
}
