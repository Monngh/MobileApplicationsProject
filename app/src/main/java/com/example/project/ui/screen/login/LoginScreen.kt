package com.example.project.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project.R
import com.example.project.ui.components.Input
import com.example.project.ui.components.PrimaryButton
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.TextSecondary
import com.example.project.ui.theme.UptelBlue
import com.example.project.ui.theme.appBackgroundGradient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onOpenDrawer: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
) {
    // --- ESTADO LOCAL (PURO DISEÑO) ---
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    // Añadido de nuevo para el Checkbox
    var rememberMe by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_warehouse_24),
                        contentDescription = "Logo UPTEL",
                        modifier = Modifier.height(32.dp),
                        contentScale = ContentScale.Fit
                    )
                },
                actions = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        // Contenedor principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(appBackgroundGradient)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.PaddingMedium),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

            // --- ILUSTRACIÓN GRANDE ---
            Image(
                painter = painterResource(id = R.drawable.ou), // Tu ilustración
                contentDescription = "Ilustración de Bienvenida",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Ajusta esta altura como quieras
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

            // --- Títulos ---
            Text(
                text = "Inicia Sesión en UPTEL",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            // --- CORREGIDO: Spacer60 no existe, usamos SpacerLarge ---
            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

            // --- CAMPO DE EMAIL ---
            Input(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = "Correo electrónico",
                isError = emailError != null,
                errorMessage = emailError
            )

            Spacer(modifier = Modifier.height(Dimens.SpacerMedium)) // Espacio más corto entre inputs

            // --- CAMPO DE CONTRASEÑA ---
            Input(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = "Contraseña",
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, "Toggle password visibility")
                    }
                },
                isError = passwordError != null,
                errorMessage = passwordError
            )

            // --- CORREGIDO: "Recordarme" y "Olvidaste..." ---
            // Este Row va DEBAJO de la contraseña, como en el diseño
            Spacer(modifier = Modifier.height(Dimens.SpacerSmall))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it }
                    )
                    Text("Recordarme", style = MaterialTheme.typography.bodyMedium)
                }

                TextButton(onClick = onNavigateToForgotPassword) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // --- CORREGIDO: Spacer60 no existe, usamos SpacerLarge ---
            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

            // --- BOTÓN DE INICIAR SESIÓN ---
            PrimaryButton(
                text = "Iniciar Sesión",
                onClick = {
                    if (email.isBlank()) { emailError = "El correo es requerido" }
                    if (password.isBlank()) { passwordError = "La contraseña es requerida" }
                    if (email.isNotBlank() && password.isNotBlank()) { onLoginSuccess() }
                },
            )

            // --- CORREGIDO: "Regístrate" movido aquí ---
            // Va DEBAJO del botón de Iniciar Sesión, centrado
            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tienes una cuenta? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                TextButton(onClick = onNavigateToRegister, contentPadding = PaddingValues(0.dp)) {
                    Text(
                        text = "Regístrate",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = UptelBlue
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.PaddingMedium)) // Padding final
        }
    }
}