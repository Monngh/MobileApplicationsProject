package com.example.project.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

// Colores base de tu diseño (extraídos de la imagen)
val UptelBlue = Color(0xFF0D47A1) // Un azul corporativo fuerte para botones
val UptelBlueLight = Color(0xFF1565C0)
val TextPrimary = Color(0xFF212121) // Para títulos
val TextSecondary = Color(0xFF757575) // Para subtítulos y etiquetas
val BackgroundLight = Color(0xFFFFFFFF)
val BorderGray = Color(0xFFBDBDBD) // Para bordes de inputs
val ErrorRed = Color(0xFFD32F2F) // <-- AÑADE ESTA LÍNEA (Rojo para errores)

// --- ¡LA MEJORA! ---
// Colores para el degradado del fondo
val GradientStart = Color(0xFFF4F8FF) // Un azul muy muy claro
val GradientEnd = Color(0xFFFFFFFF) // Blanco puro

// Pincel del degradado que reutilizaremos
val appBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        GradientStart,
        GradientEnd
    )
)
// ... (Tus otros colores: UptelBlue, TextSecondary, etc.)

val LightGray = Color(0xFFF0F0F0) // Un gris muy claro para botones inactivos


// ... (Tus colores GradientEnd, ErrorRed, etc.)

// --- COLORES PARA MODO OSCURO ---
val DarkBackground = Color(0xFF121212) // Un negro estándar
val DarkSurface = Color(0xFF1E1E1E) // Un gris oscuro para tarjetas
val DarkTextPrimary = Color(0xFFE0E0E0) // Un texto casi blanco
val UptelBlueDark = Color(0xFF82B1FF) // Un azul más claro para que resalte en oscuro


val UnreadNotificationBg = Color(0xFFF4F8FF)