package com.example.project.ui.screen.container

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.project.ui.screen.about.AboutScreen
import com.example.project.ui.screen.chat_detail.ChatDetailScreen
import com.example.project.ui.screen.contact.ContactScreen
import com.example.project.ui.screen.detail.PropertyDetailPage
import com.example.project.ui.screen.edit_profile.EditProfileScreen
import com.example.project.ui.screen.forgot_password.ForgotPasswordScreen
import com.example.project.ui.screen.how_it_works.HowItWorksScreen
import com.example.project.ui.screen.login.LoginScreen
import com.example.project.ui.screen.login.LoginViewModel
import com.example.project.ui.screen.main.MainScreen
import com.example.project.ui.screen.pages.NotificationPage
import com.example.project.ui.screen.pages.profile.ProfileViewModel
import com.example.project.ui.screen.pages.profile.ProfileViewModelFactory
import com.example.project.ui.screen.register.RegisterScreen
import com.example.project.ui.screen.register.RegisterViewModel
import com.example.project.ui.screen.welcome.WelcomeScreen
import kotlinx.coroutines.launch

@Composable
fun ScreenContainer() {
    val context = LocalContext.current
    val navController = rememberNavController()

    // AuthViewModel - Gestión Global de Autenticación
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context)
    )

    val authState by authViewModel.authState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Estados del Drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Funciones de Control
    val onOpenDrawer: () -> Unit = {
        scope.launch { drawerState.open() }
    }

    val onCloseDrawer: () -> Unit = {
        scope.launch { drawerState.close() }
    }

    val onNavigateTo: (String) -> Unit = { route ->
        navController.navigate(route) {
            launchSingleTop = true
        }
        onCloseDrawer()
    }

    val onLogout: () -> Unit = {
        authViewModel.logout()
        navController.navigate(NavGraph.Welcome.route) {
            popUpTo(0) { inclusive = true }
        }
        onCloseDrawer()
    }

    // UI Principal
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                isUserLoggedIn = authViewModel.isLoggedIn(),
                currentUser = currentUser,
                onNavigateTo = onNavigateTo,
                onCloseDrawer = onCloseDrawer,
                onLogout = onLogout
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = when (authState) {
                is AuthState.Authenticated -> NavGraph.Main.createRoute()  // Usa ruta con query param
                is AuthState.Unauthenticated -> NavGraph.Welcome.route
                is AuthState.Checking -> NavGraph.Welcome.route
            }
        ) {
            // ========================================
            // PANTALLAS DE AUTENTICACIÓN
            // ========================================
            composable(NavGraph.Welcome.route) {
                WelcomeScreen(
                    onNavigateToRegister = {
                        navController.navigate(NavGraph.Register.route)
                    },
                    onNavigateToLogin = {
                        navController.navigate(NavGraph.Login.route)
                    }
                )
            }

            composable(NavGraph.Login.route) {
                val loginViewModel: LoginViewModel = viewModel()
                LoginScreen(
                    onOpenDrawer = onOpenDrawer,
                    onNavigateToForgotPassword = {
                        navController.navigate(NavGraph.ForgotPassword.route)
                    },
                    onNavigateToRegister = {
                        navController.navigate(NavGraph.Register.route)
                    },
                    onLoginSuccess = { token, user ->
                        authViewModel.onLoginSuccess(token, user)
                        navController.navigate(NavGraph.Main.createRoute()) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    viewModel = loginViewModel
                )
            }

            composable(NavGraph.Register.route) {
                val registerViewModel: RegisterViewModel = viewModel()
                RegisterScreen(
                    onOpenDrawer = onOpenDrawer,
                    onNavigateToLogin = {
                        navController.navigate(NavGraph.Login.route)
                    },
                    onNavigateToTerms = { /* TODO */ },
                    onNavigateToPrivacy = { /* TODO */ },
                    onRegisterSuccess = { token, user ->
                        authViewModel.onLoginSuccess(token, user)
                        navController.navigate(NavGraph.Main.createRoute()) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    viewModel = registerViewModel
                )
            }

            composable(NavGraph.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onOpenDrawer = onOpenDrawer,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // ========================================
            // PANTALLA PRINCIPAL (CON BOTTOM NAV)
            // ========================================
            composable(
                route = "main_screen?tab={tab}",
                arguments = listOf(navArgument("tab") { 
                    type = NavType.StringType
                    defaultValue = NavGraph.Home.route
                })
            ) { backStackEntry ->
                val startTab = backStackEntry.arguments?.getString("tab") ?: NavGraph.Home.route
                MainScreen(
                    authViewModel = authViewModel,
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onNavigateToChatDetail = { userId, userName ->
                        navController.navigate(NavGraph.ChatDetail.createRoute(userId, userName))
                    },
                    onNavigateToPropertyDetail = { propertyId ->
                        navController.navigate(NavGraph.PropertyDetail.createRoute(propertyId))
                    },
                    onNavigateToEditProfile = {
                        navController.navigate(NavGraph.EditProfile.route)
                    },
                    onNavigateToNotifications = {
                        navController.navigate(NavGraph.Notifications.route)
                    },
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(NavGraph.Welcome.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    startDestination = startTab
                )
            }

            // ========================================
            // PANTALLAS DE DETALLE
            // ========================================
            composable(
                route = NavGraph.PropertyDetail.route,
                arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
            ) { backStackEntry ->
                val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
                PropertyDetailPage(
                    propertyId = propertyId,
                    onNavigateBack = { navController.popBackStack() },
                    onCallOwner = { /* TODO: Implementar llamada */ },
                    onMessageOwner = { ownerId, ownerName ->
                        // Navegar al chat con el propietario
                        navController.navigate(NavGraph.ChatDetail.createRoute(ownerId.toString(), ownerName))
                    }
                )
            }

            composable(
                route = NavGraph.ChatDetail.route,
                arguments = listOf(
                    navArgument("chatId") { type = NavType.StringType },
                    navArgument("ownerName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                val ownerName = java.net.URLDecoder.decode(
                    backStackEntry.arguments?.getString("ownerName") ?: "Usuario",
                    "UTF-8"
                )
                ChatDetailScreen(
                    chatId = chatId,
                    token = authViewModel.getToken(),
                    currentUserId = authViewModel.getCurrentUserId(),
                    otherUserName = ownerName,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // ========================================
            // EDITAR PERFIL
            // ========================================
            composable(NavGraph.EditProfile.route) {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val profileViewModel: ProfileViewModel = viewModel(
                    factory = ProfileViewModelFactory(context)
                )
                val uiState by profileViewModel.uiState.collectAsState()
                var showLogoutMessage by remember { mutableStateOf(false) }
                val snackbarHostState = remember { SnackbarHostState() }

                // Inicializar token
                LaunchedEffect(Unit) {
                    profileViewModel.initialize(authViewModel.getToken())
                }

                // Mostrar error si hay
                LaunchedEffect(uiState.error) {
                    uiState.error?.let { error ->
                        snackbarHostState.showSnackbar(
                            message = error,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        EditProfileScreen(
                            currentName = uiState.profile?.name ?: "",
                            currentEmail = uiState.profile?.email ?: "",
                            currentPhone = uiState.profile?.phone ?: "",
                            onSave = { name, email, phone, password, newPwd ->
                                profileViewModel.updateProfile(name, email, phone, password ?: "", newPwd) {
                                    // ✅ Ejecutar en el scope correcto (main thread)
                                    scope.launch {
                                        showLogoutMessage = true
                                        kotlinx.coroutines.delay(2000)

                                        // ✅ Logout primero
                                        authViewModel.logout()

                                        // ✅ Luego navegar
                                        navController.navigate(NavGraph.Login.route) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                }
                            },
                            onDeleteAccount = { password ->
                                profileViewModel.deleteAccount(password) {
                                    scope.launch {
                                        authViewModel.logout()
                                        kotlinx.coroutines.delay(300)
                                        navController.navigate(NavGraph.Welcome.route) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                }
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )

                        // Mensaje de éxito antes de cerrar sesión
                        if (showLogoutMessage) {
                            AlertDialog(
                                onDismissRequest = {},
                                icon = {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        null,
                                        tint = Color(0xFF4CAF50)
                                    )
                                },
                                title = { Text("¡Cambios guardados!") },
                                text = {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Cerrando sesión por seguridad...")
                                        Spacer(modifier = Modifier.height(8.dp))
                                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                    }
                                },
                                confirmButton = {}
                            )
                        }
                    }
                }
            }






            // ========================================
            // PANTALLAS DE INFORMACIÓN
            // ========================================
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
        }
    }
}
