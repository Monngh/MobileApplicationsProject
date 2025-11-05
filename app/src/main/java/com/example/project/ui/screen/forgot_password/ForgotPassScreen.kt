package com.example.project.ui.screen.forgot_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
@Preview
@Composable
fun ForgotPasswordScreen(
    onOpenDrawer: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    // --- ESTADO LOCAL (PURO DISEÑO) ---
    var email by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }

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

            // --- ESTA ES LA CLAVE PARA LA DISTRIBUCIÓN ---
            // Distribuye el espacio para que no se quede todo arriba
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- 1. ILUSTRACIÓN (para consistencia) ---
            Image(
                painter = painterResource(id = R.drawable.ou), // Reutilizamos la ilustración
                contentDescription = "Ilustración de Recuperación",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp), // Un poco más pequeña
                contentScale = ContentScale.Fit
            )

            // --- 2. TÍTULOS ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Recuperar contraseña",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(Dimens.SpacerSmall))
                Text(
                    text = "Te enviaremos un enlace para restablecer tu contraseña",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            // --- 3. CAMPO DE EMAIL ---
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

            // --- 4. BOTÓN PRINCIPAL ---
            PrimaryButton(
                text = "Enviar instrucciones",
                onClick = {
                    if (email.isBlank()) { emailError = "El correo es requerido" }
                    // Lógica de envío...
                }
            )

            // --- 5. LINK PARA VOLVER ---
            TextButton(onClick = onNavigateBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = UptelBlue
                )
                Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
                Text(
                    text = "Volver a iniciar sesión",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = UptelBlue
                    )
                )
            }

            Spacer(modifier = Modifier.height(Dimens.PaddingMedium)) // Padding final
        }
    }
}