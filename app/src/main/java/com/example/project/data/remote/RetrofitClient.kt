package com.example.project.data.remote

import com.example.project.data.remote.api.ApiService
import com.example.project.data.repository.AuthRepository
import com.example.project.data.repository.FavoriteRepository
import com.example.project.data.repository.MessageRepository
import com.example.project.data.repository.PropertyRepository
import com.example.project.data.repository.UserRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // private const val BASE_URL = "http://10.0.2.2:8000/api/"
    // Para dispositivo físico, usa tu IP local:
    private const val BASE_URL = "http://192.168.68.169:8000/api/"

    // Logging interceptor para debug
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente HTTP con configuración de timeouts
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Instancia de Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // ========================================
    // API SERVICES - Instancias lazy
    // ========================================

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    private val favoriteApiService: FavoriteApiService by lazy {
        retrofit.create(FavoriteApiService::class.java)
    }

    // ========================================
    // REPOSITORIOS - Factory methods
    // ========================================

    /**
     * Crea instancia del repositorio de autenticación
     */
    fun createAuthRepository(): AuthRepository {
        return AuthRepository(apiService)
    }

    /**
     * Crea instancia del repositorio de propiedades
     */
    fun createPropertyRepository(): PropertyRepository {
        return PropertyRepository(apiService)
    }

    /**
     * Crea instancia del repositorio de favoritos
     */
    fun createFavoriteRepository(): FavoriteRepository {
        return FavoriteRepository(favoriteApiService)
    }

    /**
     * Crea instancia del repositorio de mensajes
     */
    fun createMessageRepository(): MessageRepository {
        return MessageRepository(apiService)
    }

    /**
     * Crea instancia del repositorio de usuarios
     */
    fun createUserRepository(): UserRepository {
        return UserRepository(apiService)
    }

    /**
     * Obtener instancia del ApiService directamente
     */
    fun api(): ApiService {
        return apiService
    }
}
