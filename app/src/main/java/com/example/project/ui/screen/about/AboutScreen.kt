package com.example.project.ui.screen.about

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
fun AboutScreen(onOpenDrawer: () -> Unit) {
    InfoPageTemplate(
        title = "Acerca de",
        onOpenDrawer = onOpenDrawer
    ) {
        // --- Contenido Específico de esta página ---
        Text(
            text = "Bienvenido a [Nombre de tu App]",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
        Text(
            text = "Nuestra misión es conectar a estudiantes con los mejores alojamientos cerca de la UPP. Entendemos que encontrar el lugar perfecto para vivir es crucial para tu éxito académico y personal.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
        Text(
            text = "Esta aplicación es un proyecto diseñado para facilitar esa búsqueda, ofreciendo listados verificados, filtros detallados y contacto directo con los propietarios.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}