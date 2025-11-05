package com.example.project.ui.screen.container
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.project.R
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.ErrorRed
import com.example.project.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    onNavigateTo: (route: String) -> Unit,
    onCloseDrawer: () -> Unit,
    onLogout: () -> Unit
) {
    // --- SIMULACIÓN DE ESTADO ---
    // Cambia esto a 'false' para ver el diseño de "logged-out"
    var isLoggedIn by rememberSaveable { mutableStateOf(true) }

    // Para saber qué item está seleccionado
    var selectedRoute by rememberSaveable { mutableStateOf(NavGraph.Home.route) }

    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.85f) // El menú no ocupa toda la pantalla
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // --- 1. Encabezado ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.PaddingMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_app), // Tu logo
                    contentDescription = "Logo UPTEL",
                    modifier = Modifier.height(32.dp),
                    contentScale = ContentScale.Fit
                )
                IconButton(onClick = onCloseDrawer) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar menú")
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacerSmall))
            Divider()

            // --- 2. Contenido del Menú ---
            if (isLoggedIn) {
                // --- MENÚ DE USUARIO LOGUEADO ---
                DrawerSection(title = "Principal")
                DrawerItem(
                    label = "Inicio",
                    icon = Icons.Outlined.Home,
                    isSelected = selectedRoute == NavGraph.Home.route,
                    onClick = {
                        selectedRoute = NavGraph.Home.route
                        onNavigateTo(NavGraph.Home.route)
                    }
                )

                DrawerSection(title = "Tu Cuenta")
                DrawerItem(
                    label = "Mi Perfil",
                    icon = Icons.Outlined.Person,
                    isSelected = selectedRoute == NavGraph.Profile.route,
                    onClick = {
                        selectedRoute = NavGraph.Profile.route
                        onNavigateTo(NavGraph.Profile.route)
                    }
                )
                DrawerItem(
                    label = "Mensajes",
                    icon = Icons.Outlined.ChatBubbleOutline,
                    isSelected = selectedRoute == NavGraph.Messages.route,
                    onClick = {
                        selectedRoute = NavGraph.Messages.route
                        onNavigateTo(NavGraph.Messages.route)
                    }
                )
                DrawerItem(
                    label = "Notificaciones",
                    icon = Icons.Outlined.Notifications,
                    isSelected = selectedRoute == NavGraph.Notifications.route,
                    badgeCount = 2, // ¡El badge de la imagen!
                    onClick = {
                        selectedRoute = NavGraph.Notifications.route
                        onNavigateTo(NavGraph.Notifications.route)
                    }
                )

            } else {
                // --- MENÚ DE INVITADO ---
                DrawerSection(title = "Bienvenido")
                DrawerItem(
                    label = "Iniciar Sesión",
                    icon = Icons.Outlined.Login,
                    isSelected = selectedRoute == NavGraph.Login.route,
                    onClick = {
                        selectedRoute = NavGraph.Login.route
                        onNavigateTo(NavGraph.Login.route)
                    }
                )
                DrawerItem(
                    label = "Registrarse",
                    icon = Icons.Outlined.AppRegistration,
                    isSelected = selectedRoute == NavGraph.Register.route,
                    onClick = {
                        selectedRoute = NavGraph.Register.route
                        onNavigateTo(NavGraph.Register.route)
                    }
                )
            }

            DrawerSection(title = "Información")
            DrawerItem(
                label = "Acerca de",
                icon = Icons.Outlined.Info,
                isSelected = selectedRoute == NavGraph.About.route,
                onClick = {
                    selectedRoute = NavGraph.About.route
                    onNavigateTo(NavGraph.About.route)
                }
            )
            DrawerItem(
                label = "Cómo Funciona",
                icon = Icons.Outlined.HelpOutline,
                isSelected = selectedRoute == NavGraph.HowItWorks.route,
                onClick = {
                    selectedRoute = NavGraph.HowItWorks.route
                    onNavigateTo(NavGraph.HowItWorks.route)
                }
            )
            DrawerItem(
                label = "Contacto",
                icon = Icons.Outlined.Email,
                isSelected = selectedRoute == NavGraph.Contact.route,
                onClick = {
                    selectedRoute = NavGraph.Contact.route
                    onNavigateTo(NavGraph.Contact.route)
                }
            )

            // --- 3. Footer (Cerrar Sesión) ---
            Spacer(modifier = Modifier.weight(1f)) // Empuja esto al fondo
            if (isLoggedIn) {
                Divider()
                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión", fontWeight = FontWeight.SemiBold) },
                    icon = { Icon(Icons.Default.Logout, contentDescription = null) },
                    selected = false,
                    onClick = {
                        isLoggedIn = false // Simulación de logout
                        onLogout()
                    },
                    // Colores especiales para el botón de logout
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedIconColor = ErrorRed,
                        unselectedTextColor = ErrorRed
                    ),
                    modifier = Modifier.padding(Dimens.PaddingMedium)
                )
            }
        }
    }
}


// --- COMPONENTES INTERNOS DEL DRAWER ---

@Composable
private fun DrawerSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = TextSecondary,
        modifier = Modifier.padding(horizontal = Dimens.PaddingMedium, vertical = Dimens.SpacerMedium)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawerItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    badgeCount: Int? = null
) {
    NavigationDrawerItem(
        label = { Text(label, fontWeight = FontWeight.SemiBold) },
        icon = { Icon(icon, contentDescription = null) },
        selected = isSelected,
        onClick = onClick,
        // ¡El badge para Notificaciones!
        badge = {
            if (badgeCount != null) {
                Badge { Text(badgeCount.toString()) }
            }
        },
        modifier = Modifier.padding(horizontal = Dimens.PaddingMedium)
    )
}