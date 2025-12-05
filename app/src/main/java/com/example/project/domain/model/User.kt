package com.example.project.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String? = null,
    val profileImage: String? = null,
    val accountType: String? = null  // <- Asegúrate de que esté
)
