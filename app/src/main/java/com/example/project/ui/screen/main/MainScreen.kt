package com.example.project.ui.screen.main
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.project.R
import com.example.project.ui.components.PrimaryButton
import com.example.project.ui.components.buttons.navbar.NavItem
import com.example.project.ui.screen.container.NavGraph
import com.example.project.ui.screen.main.pages.ProfilePage
import com.example.project.ui.screen.pages.HomePage
import com.example.project.ui.screen.pages.MessagesScreen
import com.example.project.ui.screen.pages.SavedScreen
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.ProjectTheme
import com.example.project.ui.theme.TextSecondary
import com.example.project.ui.theme.UptelBlue
import com.example.project.ui.theme.appBackgroundGradient
// ... (Importaciones: Scaffold, NavHost, composable, rememberNavController, NavigationBar, NavigationBarItem, Icon, Text)
// ... (Importa tu NavGraph y tus páginas: HomePage, SearchPage, etc.)
// ... (Importa HomePage.kt que acabamos de crear)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onOpenDrawer: () -> Unit,
    onNavigateToChatDetail: (String) -> Unit,
    onNavigateToPropertyDetail: (String) -> Unit,

    onLogout: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val bottomNavController = rememberNavController() // NavController para las pestañas de abajo

    Scaffold(
        topBar = {
            // --- NUEVA TOPAPPBAR MODERNA ---
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimens.CornerRadius))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                            .padding(horizontal = Dimens.PaddingSmall, vertical = Dimens.SpacerXSmall)
                            .clickable { /* TODO: Abrir selector de ubicación */ }
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Ubicación", tint = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.width(Dimens.SpacerXSmall))
                        Text(
                            text = "Kochi, Kerala", // Placeholder de ubicación
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                navigationIcon = {
                    // Mantendremos el menú lateral en el AppBar de MainScreen
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                actions = {
                    // Icono de Notificaciones con BadgedBox
                    BadgedBox(
                        badge = {
                            Badge { Text("5") } // Ejemplo de badge
                        }
                    ) {
                        IconButton(onClick = { /* TODO: Navegar a Notificaciones */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                    Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
                    // Avatar del Perfil
                    Image(
                        painter = painterResource(id = R.drawable.profile_avatar_placeholder), // Necesitas una imagen de avatar
                        contentDescription = "Perfil",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { /* TODO: Navegar a Perfil */ },
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent // Fondo transparente
                )
            )
        },
        bottomBar = {
            // --- ¡AÑADIMOS "MESSAGES" AL BOTTOM BAR! ---
            BottomNavigationBar(
                navController = bottomNavController,
                onNavigateToChatDetail = onNavigateToChatDetail // <-- Lo pasamos
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = NavGraph.Home.route
        ) {
            composable(NavGraph.Home.route) {
                HomePage(
                    paddingValues = paddingValues,
                    onNavigateToPropertyDetail = { propertyId ->
                        println("Navegar a detalles de propiedad: $propertyId")
                    }
                )
            }
            composable(NavGraph.Search.route) {
                // TODO: Crear SearchPage
                Text("Search Page", Modifier.padding(paddingValues))
            }
            composable(NavGraph.Profile.route) {
                // Ya no es un Text(), es la página real
                ProfilePage(
                    paddingValues = paddingValues,
                    onLogout = onLogout, // 1. Pasa la función de Logout
                    onNavigateToSaved = { // 2. Conecta a la bottom nav
                        bottomNavController.navigate(NavGraph.Saved.route) {
                            popUpTo(bottomNavController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    onNavigateToMessages = { // 3. Conecta a la bottom nav
                        bottomNavController.navigate(NavGraph.Messages.route) {
                            popUpTo(bottomNavController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(NavGraph.Messages.route) {
                MessagesScreen(
                    paddingValues = paddingValues,
                    onNavigateToChatDetail = onNavigateToChatDetail // <-- Le pasamos la acción
                )
            }
            composable(NavGraph.Saved.route) {
                SavedScreen(
                    paddingValues = paddingValues,
                    onNavigateToPropertyDetail = onNavigateToPropertyDetail, // <-- Pásalo aquí
                    onExploreClick = {
                        // Navega a la pestaña Home
                        bottomNavController.navigate(NavGraph.Home.route) {
                            popUpTo(bottomNavController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomNavigationBar(
    navController: NavController,
    onNavigateToChatDetail: (String) -> Unit // <-- Recibe la acción
) {
    val items = listOf(
        Pair(NavItem("Home", Icons.Default.Home, 0), NavGraph.Home.route),
        Pair(NavItem("Saves", Icons.Default.FavoriteBorder, 0), NavGraph.Saved.route),
        Pair(NavItem("Messages", Icons.Default.Message, 0), NavGraph.Messages.route),
        Pair(NavItem("Profile", Icons.Default.Person, 0), NavGraph.Profile.route),
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface, // Fondo del NavigationBar
        tonalElevation = Dimens.CardElevation // Sombra sutil
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { (navItem, route) ->
            val isSelected = currentDestination?.route == route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { // PopUpTo el inicio del BottomNavGraph
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (navItem.badgeCount > 0) {
                                Badge { Text(text = navItem.badgeCount.toString()) }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = navItem.icon,
                            contentDescription = navItem.label,
                            tint = if (isSelected) UptelBlue else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                label = { Text(navItem.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = UptelBlue,
                    selectedTextColor = UptelBlue,
                    indicatorColor = UptelBlue.copy(alpha = 0.1f) // Indicador de selección sutil
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ProjectTheme {
        MainScreen(
            onOpenDrawer = {},
            onNavigateToChatDetail = {},
            onNavigateToPropertyDetail = {},
            onLogout = {},
            onNavigateToNotifications = {}
        )
    }
}