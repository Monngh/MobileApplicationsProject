package com.example.project.ui.screen.main.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project.R
import com.example.project.ui.theme.*

// --- DATA CLASS Y DATOS DE EJEMPLO (COMO ANTES) ---
data class ProfileMessage(
    val id: Int,
    val senderName: String,
    val subject: String,
    val preview: String,
    val timestamp: String,
    val senderImage: Int
)

val messageList = listOf(
    ProfileMessage(1, "Carlos Rodríguez", "Re: Departamento Moderno", "Hola, me interesa...", "Hace 2 días", R.drawable.profile_avatar_placeholder),
    ProfileMessage(2, "Laura Sánchez", "Re: Estudio Amueblado", "¿El departamento incluye...", "Hace 5 horas", R.drawable.profile_avatar_placeholder)
)

// --- ESTA ES LA PÁGINA DE PERFIL REAL ---
@Composable
fun ProfilePage(
    paddingValues: PaddingValues, // <-- Recibe el padding del Scaffold de MainScreen
    onNavigateToSaved: () -> Unit,
    onNavigateToMessages: () -> Unit,
    onLogout: () -> Unit
) {
    // Columna principal con el fondo degradado y scroll
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackgroundGradient)
            .padding(paddingValues) // <-- Aplica el padding
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

        // --- BLOQUE 1: TARJETA DE PERFIL ---
        ProfileCard(
            modifier = Modifier.padding(horizontal = Dimens.PaddingMedium),
            onNavigateToSaved = onNavigateToSaved,
            onNavigateToMessages = onNavigateToMessages,
            onLogout = onLogout
        )

        Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

        // --- BLOQUE 2: SECCIÓN DE MENSAJES ---
        Column(
            modifier = Modifier.padding(horizontal = Dimens.PaddingMedium)
        ) {
            Text(
                text = "Mis Mensajes",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
            messageList.forEach { message ->
                MessageItemCard(
                    message = message,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
            }
        }
        Spacer(modifier = Modifier.height(Dimens.PaddingMedium)) // Padding final
    }
}


// --- COMPONENTE TARJETA DE PERFIL ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileCard(
    modifier: Modifier = Modifier,
    onNavigateToSaved: () -> Unit,
    onNavigateToMessages: () -> Unit,
    onLogout: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.CardElevation),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ou), // Placeholder
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(100.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
            Text(text = "Ana García", style = MaterialTheme.typography.headlineMedium)
            Text(text = "estudiante", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

            ProfileNavItem(
                label = "Mi Perfil",
                icon = Icons.Outlined.Person,
                isSelected = true, // Marcado como seleccionado
                onClick = { /* Ya estamos aquí */ }
            )
            ProfileNavItem(
                label = "Guardados",
                icon = Icons.Outlined.BookmarkBorder,
                isSelected = false,
                onClick = onNavigateToSaved
            )
            ProfileNavItem(
                label = "Mensajes",
                icon = Icons.Outlined.ChatBubbleOutline,
                isSelected = false,
                badgeCount = 1,
                onClick = onNavigateToMessages
            )
            ProfileNavItem(
                label = "Cerrar Sesión",
                icon = Icons.Outlined.Logout,
                isSelected = false,
                isLogout = true,
                onClick = onLogout
            )
        }
    }
}

// --- COMPONENTE ITEM DE NAVEGACIÓN (Para el ProfileCard) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    badgeCount: Int? = null,
    isLogout: Boolean = false
) {
    val textColor = if (isLogout) ErrorRed else if (isSelected) MaterialTheme.colorScheme.primary else TextPrimary
    val iconColor = if (isLogout) ErrorRed else if (isSelected) MaterialTheme.colorScheme.primary else TextSecondary

    NavigationDrawerItem(
        label = { Text(label, fontWeight = FontWeight.SemiBold) },
        icon = { Icon(icon, contentDescription = null) },
        selected = isSelected,
        onClick = onClick,
        badge = {
            if (badgeCount != null) {
                Badge { Text(badgeCount.toString()) }
            }
        },
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = UnreadNotificationBg,
            unselectedContainerColor = Color.Transparent,
            selectedIconColor = iconColor,
            selectedTextColor = textColor,
            unselectedIconColor = iconColor,
            unselectedTextColor = textColor
        ),
        modifier = Modifier.padding(bottom = Dimens.SpacerSmall)
    )
}

@Composable
private fun MessageItemCard(
    message: ProfileMessage,
    modifier: Modifier = Modifier // <-- Acepta un modifier
) {
    Card(
        // Aplicamos el modifier que pasamos
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.CardElevation / 2),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        // (El contenido interno del Row se mantiene igual)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingMedium),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(id = message.senderImage),
                contentDescription = message.senderName,
                modifier = Modifier.size(40.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(Dimens.SpacerMedium))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = message.senderName, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    Text(text = message.timestamp, style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                }
                Spacer(modifier = Modifier.height(Dimens.SpacerXSmall))
                Text(text = message.subject, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(modifier = Modifier.height(Dimens.SpacerXSmall))
                Text(text = message.preview, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, maxLines = 2)
            }
        }
    }
}