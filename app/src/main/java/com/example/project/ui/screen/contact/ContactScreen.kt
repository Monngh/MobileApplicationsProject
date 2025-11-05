package com.example.project.ui.screen.contact

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.project.ui.components.InfoPageTemplate
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.TextSecondary

@Composable
fun ContactScreen(onOpenDrawer: () -> Unit) {
    InfoPageTemplate(
        title = "Contacto",
        onOpenDrawer = onOpenDrawer
    ) {
        // --- Contenido Específico de esta página ---
        Text(
            text = "¿Preguntas o Sugerencias?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Estamos aquí para ayudarte. Contáctanos a través de los siguientes canales:",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

        // Fila de Email
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
            Column {
                Text(
                    text = "Correo Electrónico",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Text(
                    text = "soporte.uptel@tudominio.com",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.SpacerMedium))

        // Fila de Teléfono
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Teléfono",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
            Column {
                Text(
                    text = "Teléfono / WhatsApp",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Text(
                    text = "+52 (123) 456-7890",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}