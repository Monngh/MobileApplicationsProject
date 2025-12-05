package com.example.project.ui.screen.container

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.project.data.local.TokenManager
import com.example.project.data.remote.RetrofitClient
import com.example.project.data.repository.AuthRepository
import com.example.project.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository = RetrofitClient.createAuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Checking)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(500)
            val token = tokenManager.getToken()
            _authState.value = if (token != null) {
                AuthState.Authenticated
            } else {
                AuthState.Unauthenticated
            }
        }
    }

    fun onLoginSuccess(token: String, user: User) {
        viewModelScope.launch {
            tokenManager.saveToken(token)
            _currentUser.value = user
            _authState.value = AuthState.Authenticated
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.getToken()?.let { token ->
                authRepository.logout(token)
            }
            tokenManager.clearToken()
            _currentUser.value = null
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun isLoggedIn(): Boolean {
        // Verificar tanto el estado como la existencia del token
        return tokenManager.getToken() != null
    }

    fun getToken(): String? {
        return tokenManager.getToken()
    }

    fun getCurrentUserId(): Int {
        return _currentUser.value?.id ?: 0
    }

    // ✅ AGREGAR AQUÍ
    fun refreshCurrentUserFromProfile(name: String, email: String, phone: String) {
        val current = _currentUser.value ?: return
        _currentUser.value = current.copy(
            name = name,
            email = email,
            phone = phone
        )
    }

} // ← Cierre de AuthViewModel

sealed class AuthState {
    object Checking : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}

class AuthViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val tokenManager = TokenManager(context)
            return AuthViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
