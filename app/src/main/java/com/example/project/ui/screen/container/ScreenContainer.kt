package com.example.project.ui.screen.container

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project.ui.screen.about.AboutScreen
import com.example.project.ui.screen.chat_detail.ChatDetailScreen
import com.example.project.ui.screen.contact.ContactScreen
import com.example.project.ui.screen.forgot_password.ForgotPasswordScreen
import com.example.project.ui.screen.how_it_works.HowItWorksScreen
import com.example.project.ui.screen.login.LoginScreen
import com.example.project.ui.screen.main.MainScreen
import com.example.project.ui.screen.main.pages.NotificationPage // Importa tus pantallas
import com.example.project.ui.screen.register.RegisterScreen
import com.example.project.ui.screen.welcome.WelcomeScreen
import kotlinx.coroutines.launch

@Composable
fun ScreenContainer() {
    val navController = rememberNavController()

    // --- 1. ESTADO PARA EL DRAWER (MENÚ) ---
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // --- 2. ACCIONES REALES PARA EL DRAWER ---

    // Función para ABRIR el menú (esta se la pasas a tus pantallas)
    val onOpenDrawer: () -> Unit = {
        scope.launch {
            drawerState.open()
        }
    }

    // Función para CERRAR el menú
    val onCloseDrawer: () -> Unit = {
        scope.launch {
            drawerState.close()
        }
    }

    // Función que maneja la NAVEGACIÓN desde el menú
    val onNavigateTo: (String) -> Unit = { route ->
        navController.navigate(route) {
            launchSingleTop = true // Evita duplicar pantallas
        }
        onCloseDrawer()
    }

    // Función para CERRAR SESIÓN
    val onLogout: () -> Unit = {
        navController.navigate(NavGraph.Login.route) {
            popUpTo(0) // Borra toda la pila de navegación
        }
        onCloseDrawer()
    }

    // --- 3. EL COMPONENTE DEL MENÚ ---
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Aquí llamamos al Composable 'DrawerContent.kt' que creamos
            DrawerContent(
                onNavigateTo = onNavigateTo,
                onCloseDrawer = onCloseDrawer,
                onLogout = onLogout
            )
        }
    ) {

        // --- 4. TU NavHost AHORA VA AQUÍ DENTRO ---
        NavHost(
            navController = navController,
            startDestination = NavGraph.Welcome.route // <-- ¡Cambiamos a Welcome!
        ) {

            composable(NavGraph.Login.route) {
                LoginScreen(
                    onOpenDrawer = onOpenDrawer, // <-- ¡Acción real conectada!
                    onNavigateToForgotPassword = {
                        navController.navigate(NavGraph.ForgotPassword.route)
                    },
                    onNavigateToRegister = {
                        navController.navigate(NavGraph.Register.route)
                    }, // <-- ¡Acción real conectada!
                    onLoginSuccess = {
                        // Navega a la ruta "Home" (que abre tu MainScreen)
                        navController.navigate(NavGraph.Home.route) {
                            // ¡MUY IMPORTANTE!
                            // Borra toda la pila de navegación anterior (Welcome, Login)
                            // para que el usuario no pueda "volver" al login
                            // presionando el botón de atrás.
                            popUpTo(0)
                        }
                    }
                )
            }

            composable(NavGraph.Welcome.route) {
                // --- ASÍ ES COMO LO CONECTAS ---
                WelcomeScreen(
                    onNavigateToRegister = {
                        navController.navigate(NavGraph.Register.route)
                    },
                    onNavigateToLogin = {
                        navController.navigate(NavGraph.Login.route)
                    }
                )
            }

            composable(NavGraph.Register.route) {
                RegisterScreen(
                    onOpenDrawer = onOpenDrawer, // <-- ¡Acción real conectada!
                    onNavigateToLogin = { navController.navigate(NavGraph.Main.route) },
                    onNavigateToTerms = { /* TODO: Navegar a Términos */ },
                    onNavigateToPrivacy = { /* TODO: Navegar a Privacidad */ }
                )
            }

            composable(NavGraph.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onOpenDrawer = onOpenDrawer, // <-- ¡Acción real conectada!
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(NavGraph.Home.route) {
                MainScreen(
                    onOpenDrawer = onOpenDrawer,
                    onLogout = onLogout,
                    onNavigateToNotifications = {
                        navController.navigate(NavGraph.Notifications.route)
                    },
                    onNavigateToChatDetail = { chatId ->
                        // TODO: Implementar ChatDetail
                        println("Navegar a Chat: $chatId")
                    } ,
                    // --- ¡CORREGIDO! ---
                    onNavigateToPropertyDetail = { propertyId ->
                        // Ahora usa la ruta real que creamos
                        navController.navigate(NavGraph.PropertyDetail.createRoute(propertyId))
                    }
                )
            }
            // ... (Tus otros composables: Notifications, etc.)
// --- ¡AÑADIDO! ---
            // Esta es la pantalla de Detalles de Propiedad
            composable(
                route = NavGraph.PropertyDetail.route,
                // Le decimos que espere un argumento "propertyId"
                arguments = listOf(androidx.navigation.navArgument("propertyId") {
                    type = androidx.navigation.NavType.StringType
                })
            ) { backStackEntry ->

                // Extraemos el ID de la ruta
                val propertyId = backStackEntry.arguments?.getString("propertyId")

                if (propertyId != null) {
                    // (Asumiendo que PropertyDetailPage.kt existe)
                    com.example.project.ui.screen.detail.PropertyDetailPage(
                        // propertyId = propertyId, // <-- Pasa el ID si tu pantalla lo necesita
                        onNavigateBack = { navController.popBackStack() },
                        onCallOwner = { /* ... */ },
                        onMessageOwner = { /* ... */ }
                    )
                } else {
                    navController.popBackStack() // Si el ID es nulo, regresa
                }
            }
            // --- ¡AÑADE ESTE NUEVO COMPOSABLE PARA LA PANTALLA DE DETALLE! ---
            composable(
                route = "${NavGraph.ChatDetail.route}/{chatId}", // Ruta con argumento
                arguments = listOf(navArgument("chatId") { type = NavType.StringType })
            ) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                ChatDetailScreen(
                    chatId = chatId,
                    onNavigateBack = { navController.popBackStack() } // Acción para volver
                )
            }

            // --- 5. AÑADIMOS LAS NUEVAS PANTALLAS ---
            composable(NavGraph.Notifications.route) {
                NotificationPage(
                    onOpenDrawer = onOpenDrawer // <-- ¡Acción real conectada!
                )
            }
            composable(NavGraph.Notifications.route) {
                NotificationPage(
                    onOpenDrawer = onOpenDrawer
                )
            }

            composable(NavGraph.About.route) {
                AboutScreen(
                    onOpenDrawer = onOpenDrawer
                )
            }

            composable(NavGraph.HowItWorks.route) {
                HowItWorksScreen(
                    onOpenDrawer = onOpenDrawer
                )
            }

            composable(NavGraph.Contact.route) {
                ContactScreen(
                    onOpenDrawer = onOpenDrawer
                )
            }

            // ... Aquí irían tus otras rutas (Home, Profile, etc.)
            // composable(NavGraph.Home.route) { ... }
            // composable(NavGraph.Profile.route) { ... }
        }
    }
}