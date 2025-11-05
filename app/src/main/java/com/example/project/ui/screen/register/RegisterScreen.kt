package com.example.project.ui.screen.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.R // Asegúrate de tener tu logo e ilustración
import com.example.project.ui.components.Input
import com.example.project.ui.components.PrimaryButton
import com.example.project.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RegisterScreen(
    onOpenDrawer: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {}
) {
    // --- ESTADO LOCAL (PURO DISEÑO) ---
    var nombre by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var accountType by rememberSaveable { mutableStateOf("Estudiante") }
    var acceptTerms by rememberSaveable { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // Errores
    var nombreError by rememberSaveable { mutableStateOf<String?>(null) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var termsError by rememberSaveable { mutableStateOf(false) }

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

            // --- ILUSTRACIÓN (Usando la misma que en Login para consistencia) ---
            Image(
                painter = painterResource(id = R.drawable.ou), // Tu ilustración
                contentDescription = "Ilustración de Registro",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp), // Un poco más pequeña que en Login
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

            // --- Títulos ---
            Text(
                text = "Crear una cuenta en UPTEL",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.SpacerSmall))
            Text(
                text = "Únete a nuestra plataforma para encontrar tu departamento ideal",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

            // --- COMPONENTES DE REGISTRO ---

            // 1. Tipo de Cuenta
            AccountTypeToggle(
                selectedType = accountType,
                onTypeSelected = { accountType = it }
            )

            Spacer(modifier = Modifier.height(Dimens.SpacerMedium))

            // 2. Nombre completo
            Input(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = null
                },
                label = "Nombre completo",
                isError = nombreError != null,
                errorMessage = nombreError
            )

            Spacer(modifier = Modifier.height(Dimens.SpacerMedium))

            // 3. Correo
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

            Spacer(modifier = Modifier.height(Dimens.SpacerMedium))

            // 4. Contraseña
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

            Spacer(modifier = Modifier.height(Dimens.SpacerSmall))
            Text(
                text = "La contraseña debe tener al menos 8 caracteres",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(Dimens.SpacerMedium))

            // 5. Checkbox de Términos
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = acceptTerms,
                    onCheckedChange = {
                        acceptTerms = it
                        termsError = false
                    },
                    colors = CheckboxDefaults.colors(
                        // Se pone rojo si intentan registrarse sin aceptarlo
                        uncheckedColor = if (termsError) ErrorRed else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
                ClickableLegalText(
                    onTermsClick = onNavigateToTerms,
                    onPrivacyClick = onNavigateToPrivacy
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

            // 6. Botón de Crear Cuenta
            PrimaryButton(
                text = "Crear Cuenta",
                // El botón está deshabilitado hasta que acepten los términos
                enabled = acceptTerms,
                onClick = {
                    // Simulación de validación
                    if (nombre.isBlank()) nombreError = "El nombre es requerido"
                    if (email.isBlank()) emailError = "El correo es requerido"
                    if (password.isBlank()) passwordError = "La contraseña es requerida"
                    if (!acceptTerms) termsError = true

                    if (nombre.isNotBlank() && email.isNotBlank() && password.isNotBlank() && acceptTerms) {
                        // Lógica de registro...
                    }
                }
            )

            // 7. Navegación a Login
            Spacer(modifier = Modifier.height(Dimens.SpacerLarge))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes una cuenta? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                TextButton(onClick = onNavigateToLogin, contentPadding = PaddingValues(0.dp)) {
                    Text(
                        text = "Inicia sesión",
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


// --- COMPONENTES INTERNOS DE DISEÑO ---
// (Los ponemos aquí abajo para mantener limpio el composable principal)

@Composable
private fun AccountTypeToggle(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacerSmall)
    ) {
        val studentSelected = selectedType == "Estudiante"
        val propiSelected = selectedType == "Propietario"

        // Botón Estudiante
        Button(
            onClick = { onTypeSelected("Estudiante") },
            modifier = Modifier.weight(1f).height(Dimens.ButtonHeight),
            shape = RoundedCornerShape(Dimens.CornerRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (studentSelected) UptelBlue else LightGray,
                contentColor = if (studentSelected) Color.White else TextSecondary
            )
        ) {
            Icon(painterResource(id = R.drawable.baseline_warehouse_24), contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
            Text("Estudiante", style = MaterialTheme.typography.labelLarge)
        }

        // Botón Propietario
        Button(
            onClick = { onTypeSelected("Propietario") },
            modifier = Modifier.weight(1f).height(Dimens.ButtonHeight),
            shape = RoundedCornerShape(Dimens.CornerRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (propiSelected) UptelBlue else LightGray,
                contentColor = if (propiSelected) Color.White else TextSecondary
            )
        ) {
            Icon(painterResource(id = R.drawable.baseline_warehouse_24), contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
            Text("Propietario", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun ClickableLegalText(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    // Texto con links clickables
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = TextSecondary, fontSize = 13.sp)) {
            append("Acepto los ")
        }

        // Link 1: Términos
        pushStringAnnotation(tag = "TERMS", annotation = "terms")
        withStyle(style = SpanStyle(color = UptelBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)) {
            append("Términos y Condiciones")
        }
        pop()

        withStyle(style = SpanStyle(color = TextSecondary, fontSize = 13.sp)) {
            append(" y la ")
        }

        // Link 2: Privacidad
        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
        withStyle(style = SpanStyle(color = UptelBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)) {
            append("Política de Privacidad")
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                .firstOrNull()?.let { onTermsClick() }

            annotatedString.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset)
                .firstOrNull()?.let { onPrivacyClick() }
        }
    )
}