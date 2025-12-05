package com.example.project.ui.screen.container

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.project.R
import com.example.project.domain.model.User
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.ErrorRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    isUserLoggedIn: Boolean,
    currentUser: User?,
    onNavigateTo: (route: String) -> Unit,
    onCloseDrawer: () -> Unit,
    onLogout: () -> Unit
) {
    // Estado para rastrear la ruta seleccionada
    var selectedRoute by rememberSaveable { mutableStateOf(NavGraph.Home.route) }

    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.85f),
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ========================================
            // ENCABEZADO ELEGANTE DEL DRAWER
            // ========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
                ) {
                    // Header con logo y botón cerrar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Logo DOMY
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_home),
                                contentDescription = "Logo DOMY",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "DOMY",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }

                        // Botón cerrar elegante
                        IconButton(
                            onClick = onCloseDrawer,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    CircleShape
                                )
                                .size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Cerrar menú",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Mensaje de bienvenida
                    Text(
                        text = if (isUserLoggedIn) "¡Hola de nuevo!" else "¡Bienvenido!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = if (isUserLoggedIn) "Nos alegra verte de nuevo" else "Descubre tu hogar universitario ideal",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    )
                }
            }

            // ========================================
            // PERFIL DEL USUARIO ELEGANTE (Si está logueado)
            // ========================================
            if (isUserLoggedIn && currentUser != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp),
                            clip = true
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar elegante
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                        )
                                    ),
                                    CircleShape
                                )
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUser.name.firstOrNull()?.uppercase() ?: "U",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Info del usuario
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = currentUser.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentUser.email,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                ),
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Badge de tipo de cuenta
                            Surface(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                currentUser.accountType?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Espaciador cuando no hay usuario
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ========================================
            // SECCIÓN DE AUTENTICACIÓN ELEGANTE (Para invitados)
            // ========================================
            // ========================================
            // SECCIÓN DE AUTENTICACIÓN ELEGANTE (Para invitados)
            // ========================================
            if (!isUserLoggedIn) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Comienza tu experiencia",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        ),
                        modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                    )

                    // Tarjeta de Iniciar Sesión
                    Surface(
                        onClick = {
                            selectedRoute = NavGraph.Login.route
                            onNavigateTo(NavGraph.Login.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        border = BorderStroke(
                            1.5.dp,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Login,
                                contentDescription = "Iniciar Sesión",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Iniciar Sesión",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }

                    // Tarjeta de Registrarse
                    Surface(
                        onClick = {
                            selectedRoute = NavGraph.Register.route
                            onNavigateTo(NavGraph.Register.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.AppRegistration,
                                contentDescription = "Registrarse",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Registrarse",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // ========================================
            // CONTENIDO DEL MENÚ ELEGANTE
            // ========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                if (isUserLoggedIn) {
                    // --- MENÚ DE USUARIO LOGUEADO ---

                    DrawerSectionElegante(title = "Principal")

                    DrawerItemElegante(
                        label = "Inicio",
                        icon = Icons.Outlined.Home,
                        isSelected = selectedRoute == NavGraph.Home.route,
                        onClick = {
                            selectedRoute = NavGraph.Home.route
                            onNavigateTo(NavGraph.Main.createRoute(NavGraph.Home.route))
                        }
                    )

                    DrawerItemElegante(
                        label = "Guardados",
                        icon = Icons.Outlined.BookmarkBorder,
                        isSelected = selectedRoute == NavGraph.Saved.route,
                        onClick = {
                            selectedRoute = NavGraph.Saved.route
                            onNavigateTo(NavGraph.Main.createRoute(NavGraph.Saved.route))
                        }
                    )

                    DrawerSectionElegante(title = "Tu Cuenta")

                    DrawerItemElegante(
                        label = "Mi Perfil",
                        icon = Icons.Outlined.Person,
                        isSelected = selectedRoute == NavGraph.Profile.route,
                        onClick = {
                            selectedRoute = NavGraph.Profile.route
                            onNavigateTo(NavGraph.Main.createRoute(NavGraph.Profile.route))
                        }
                    )

                    DrawerItemElegante(
                        label = "Mensajes",
                        icon = Icons.Outlined.ChatBubbleOutline,
                        isSelected = selectedRoute == NavGraph.Messages.route,
                        badgeCount = 3,
                        onClick = {
                            selectedRoute = NavGraph.Messages.route
                            onNavigateTo(NavGraph.Main.createRoute(NavGraph.Messages.route))
                        }
                    )

                    DrawerItemElegante(
                        label = "Notificaciones",
                        icon = Icons.Outlined.Notifications,
                        isSelected = selectedRoute == NavGraph.Notifications.route,
                        badgeCount = 2,
                        onClick = {
                            selectedRoute = NavGraph.Notifications.route
                            onNavigateTo(NavGraph.Notifications.route)
                        }
                    )
                }

                // --- INFORMACIÓN ELEGANTE ---
                DrawerSectionElegante(title = "Información")

                DrawerItemElegante(
                    label = "Acerca de",
                    icon = Icons.Outlined.Info,
                    isSelected = selectedRoute == NavGraph.About.route,
                    onClick = {
                        selectedRoute = NavGraph.About.route
                        onNavigateTo(NavGraph.About.route)
                    }
                )

                DrawerItemElegante(
                    label = "Cómo Funciona",
                    icon = Icons.Outlined.HelpOutline,
                    isSelected = selectedRoute == NavGraph.HowItWorks.route,
                    onClick = {
                        selectedRoute = NavGraph.HowItWorks.route
                        onNavigateTo(NavGraph.HowItWorks.route)
                    }
                )

                DrawerItemElegante(
                    label = "Contacto",
                    icon = Icons.Outlined.Email,
                    isSelected = selectedRoute == NavGraph.Contact.route,
                    onClick = {
                        selectedRoute = NavGraph.Contact.route
                        onNavigateTo(NavGraph.Contact.route)
                    }
                )
            }

            // ========================================
            // FOOTER ELEGANTE: CERRAR SESIÓN
            // ========================================
            Spacer(modifier = Modifier.weight(1f))

            if (isUserLoggedIn) {
                Divider(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Surface(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = ErrorRed.copy(alpha = 0.1f),
                    border = BorderStroke(
                        1.5.dp,
                        ErrorRed.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "Cerrar Sesión",
                            tint = ErrorRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Cerrar Sesión",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium,
                                color = ErrorRed
                            )
                        )
                    }
                }
            }

            // Footer con versión
            Text(
                text = "DOMY v1.0",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, top = 16.dp)
                    .padding(horizontal = 24.dp)
            )
        }
    }
}

// ========================================
// COMPONENTES INTERNOS ELEGANTES
// ========================================

@Composable
private fun DrawerSectionElegante(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        ),
        modifier = Modifier.padding(
            horizontal = 28.dp,
            vertical = 12.dp
        )
    )
}

@Composable
private fun DrawerItemElegante(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    badgeCount: Int? = null
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                ),
                modifier = Modifier.weight(1f)
            )

            // Badge
            if (badgeCount != null && badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(ErrorRed, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (badgeCount > 9) "9+" else badgeCount.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
