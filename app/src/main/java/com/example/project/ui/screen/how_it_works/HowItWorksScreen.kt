package com.example.project.ui.screen.how_it_works

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.project.ui.components.InfoPageTemplate
import com.example.project.ui.theme.Dimens

@Composable
fun HowItWorksScreen(onOpenDrawer: () -> Unit) {
    InfoPageTemplate(
        title = "Cómo Funciona",
        onOpenDrawer = onOpenDrawer
    ) {
        // --- Contenido Específico de esta página ---
        Text(
            text = "1. Busca y Explora",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Usa nuestra barra de búsqueda y filtros para encontrar propiedades por ubicación, precio o tipo de habitación.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

        Text(
            text = "2. Contacta Directamente",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Una vez que encuentres un lugar que te guste, usa la pantalla de chat para hablar directamente con el propietario o arrendador.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

        Text(
            text = "3. ¡Múdate!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Coordina la visita, firma el contrato y prepárate para tu nueva vida cerca del campus.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}