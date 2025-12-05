package com.example.project.ui.screen.main.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project.ui.screen.pages.profile.ProfileViewModel  // ← IMPORTAR
import com.example.project.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    paddingValues: PaddingValues,
    token: String?,
    onNavigateToSaved: () -> Unit,
    onNavigateToMessages: () -> Unit,
    onNavigateToNotifications: () -> Unit,  // ← NUEVO
    onEditProfile: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(token) {              // ← CAMBIAR Unit por token
        viewModel.initialize(token)       // ← PASAR TOKEN REAL
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header con avatar elegante
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                )
                            ),
                            CircleShape
                        )
                        .clip(CircleShape)
                        .shadow(8.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.profile?.name?.firstOrNull()?.uppercase() ?: "U",  // ← USAR uiState.profile
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = uiState.profile?.name ?: "Cargando...",  // ← USAR uiState.profile
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Text(
                    text = uiState.profile?.email ?: "",  // ← USAR uiState.profile
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = uiState.profile?.accountType ?: "Usuario",  // ← USAR uiState.profile
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            // Tarjeta de estadísticas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp),
                        clip = true
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCardElegante(
                        value = uiState.profile?.propertiesCount?.toString() ?: "0",  // ← USAR uiState.profile
                        label = "Propiedades",
                        color = MaterialTheme.colorScheme.primary
                    )

                    Divider(
                        modifier = Modifier
                            .height(60.dp)
                            .width(1.dp),
                        color = Color(0xFFE2E8F0)
                    )

                    StatCardElegante(
                        value = uiState.profile?.favoritesCount?.toString() ?: "0",  // ← USAR uiState.profile
                        label = "Favoritos",
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Divider(
                        modifier = Modifier
                            .height(60.dp)
                            .width(1.dp),
                        color = Color(0xFFE2E8F0)
                    )

                    StatCardElegante(
                        value = "0",  // TODO: Agregar messagesCount en backend
                        label = "Mensajes",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            // Opciones de menú
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(16.dp),
                        clip = true
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    ProfileMenuItemElegante(
                        icon = Icons.Outlined.Person,
                        title = "Editar Perfil",
                        subtitle = "Actualiza tu información personal",
                        onClick = onEditProfile  // ← USAR CALLBACK
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F5F9))

                    ProfileMenuItemElegante(
                        icon = Icons.Outlined.BookmarkBorder,
                        title = "Mis Favoritos",
                        subtitle = "Propiedades guardadas",
                        onClick = onNavigateToSaved
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F5F9))

                    ProfileMenuItemElegante(
                        icon = Icons.Outlined.ChatBubbleOutline,
                        title = "Mensajes",
                        subtitle = "Conversaciones activas",
                        onClick = onNavigateToMessages
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F5F9))

                    ProfileMenuItemElegante(
                        icon = Icons.Outlined.Notifications,
                        title = "Notificaciones",
                        subtitle = "Ver tus notificaciones",
                        onClick = onNavigateToNotifications
                    )
                }
            }



            // Botón de cerrar sesión
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(16.dp),
                        clip = true
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
            ) {
                ProfileMenuItemElegante(
                    icon = Icons.Outlined.ExitToApp,
                    title = "Cerrar Sesión",
                    subtitle = "Salir de tu cuenta",
                    onClick = onLogout,
                    iconColor = MaterialTheme.colorScheme.error,
                    textColor = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .width(140.dp)
                        .shadow(12.dp, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 3.dp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Cargando...",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCardElegante(
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        )
    }
}

@Composable
private fun ProfileMenuItemElegante(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        iconColor.copy(alpha = 0.1f),
                        CircleShape
                    )
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = textColor
                    )
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            Icon(
                Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
    }
}
