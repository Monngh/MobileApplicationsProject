package com.example.project.ui.screen.main

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.project.R
import com.example.project.ui.screen.container.AuthViewModel
import com.example.project.ui.screen.container.NavGraph
import com.example.project.ui.screen.main.pages.ProfilePage
import com.example.project.ui.screen.pages.HomePage
import com.example.project.ui.screen.pages.MessagesScreen
import com.example.project.ui.screen.pages.SavedScreen
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.UptelBlue
import com.example.project.ui.screen.pages.NotificationPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    onOpenDrawer: () -> Unit,
    onNavigateToChatDetail: (String, String) -> Unit,  // (userId, userName)
    onNavigateToPropertyDetail: (String) -> Unit,
    onLogout: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToNotifications: () -> Unit = {},
    startDestination: String = NavGraph.Home.route
)
 {
    val bottomNavController = rememberNavController()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Estado para el badge de notificaciones
    var notificationCount by remember { mutableStateOf(3) }

    Scaffold(
        topBar = {
            ModernTopBar(
                onOpenDrawer = onOpenDrawer,
                onNotificationClick = {
                    onNavigateToNotifications()
                    notificationCount = 0
                },
                onProfileClick = {
                    bottomNavController.navigate(NavGraph.Profile.route) {
                        popUpTo(bottomNavController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                notificationCount = notificationCount,
                userInitial = currentUser?.name?.firstOrNull()?.uppercase() ?: "U"
            )
        },
        bottomBar = {
            AnimatedBottomNavigationBar(
                navController = bottomNavController
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavGraph.Home.route) {
                HomePage(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToPropertyDetail = onNavigateToPropertyDetail
                )
            }

            composable(NavGraph.Saved.route) {
                SavedScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToPropertyDetail = onNavigateToPropertyDetail,
                    onExploreClick = {
                        bottomNavController.navigate(NavGraph.Home.route) {
                            popUpTo(bottomNavController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(NavGraph.Messages.route) {
                MessagesScreen(
                    paddingValues = paddingValues,
                    onNavigateToChatDetail = onNavigateToChatDetail,
                    token = authViewModel.getToken()
                )

            }

            composable(NavGraph.Profile.route) {
                ProfilePage(
                    paddingValues = paddingValues,
                    token = authViewModel.getToken(),
                    onNavigateToSaved = {
                        bottomNavController.navigate(NavGraph.Saved.route) {
                            popUpTo(bottomNavController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    onNavigateToMessages = {
                        bottomNavController.navigate(NavGraph.Messages.route) {
                            popUpTo(bottomNavController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    onNavigateToNotifications = onNavigateToNotifications,
                    onEditProfile = onNavigateToEditProfile,
                    onLogout = onLogout
                )
            }

        }
    }
}

// ========================================
// TOP BAR MODERNA Y MEJORADA
// ========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernTopBar(
    onOpenDrawer: () -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
    notificationCount: Int,
    userInitial: String
) {
    // Variable para simular ubicación (después conectar con API/GPS)
    var currentLocation by remember { mutableStateOf("San Luis Potosí") }

    CenterAlignedTopAppBar(
        title = {
            // Título o Logo central si se requiere, o vacío.
            // Por ahora lo dejamos vacío para limpiar la UI como pidió el usuario.
        },
        navigationIcon = {
            IconButton(onClick = onOpenDrawer) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            // Botón de notificaciones con badge animado
            Box(modifier = Modifier.padding(end = 8.dp)) {
                BadgedBox(
                    badge = {
                        // Mostrar badge solo si hay notificaciones
                        if (notificationCount > 0) {
                            Badge(
                                containerColor = Color.Red,
                                modifier = Modifier.offset(x = (-8).dp, y = 8.dp)
                            ) {
                                Text(
                                    text = if (notificationCount > 9) "9+" else notificationCount.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                ) {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            // Avatar del usuario
            Surface(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onProfileClick),
                color = UptelBlue,
                shadowElevation = 4.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = userInitial,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

// ========================================
// BOTTOM NAVIGATION BAR CON ANIMACIONES
// ========================================
@Composable
private fun AnimatedBottomNavigationBar(
    navController: NavController
) {
    // Definir items del bottom bar
    val items = listOf(
        BottomNavItem("Inicio", Icons.Default.Home, NavGraph.Home.route),
        BottomNavItem("Guardados", Icons.Default.BookmarkBorder, NavGraph.Saved.route),
        BottomNavItem("Mensajes", Icons.Default.Message, NavGraph.Messages.route, badgeCount = 2),
        BottomNavItem("Perfil", Icons.Default.Person, NavGraph.Profile.route)
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier.height(70.dp)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            items.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } == true

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (item.badgeCount > 0) {
                                    Badge(
                                        containerColor = Color.Red
                                    ) {
                                        Text(
                                            text = item.badgeCount.toString(),
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(if (isSelected) 28.dp else 24.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UptelBlue,
                        selectedTextColor = UptelBlue,
                        indicatorColor = UptelBlue.copy(alpha = 0.15f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

// ========================================
// DATA CLASS PARA ITEMS DEL BOTTOM NAV
// ========================================
private data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
    val badgeCount: Int = 0
)
