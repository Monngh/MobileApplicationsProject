package com.example.project.ui.screen.welcome

// --- Importaciones Clave ---
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// --- Importaciones de TU proyecto (asumiendo nombres) ---
// Estas importaciones dependen de cómo hayas nombrado tus archivos
// de tema, componentes y recursos.
import com.example.project.R
import com.example.project.ui.components.PrimaryButton
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.TextSecondary
import com.example.project.ui.theme.UptelBlue
import com.example.project.ui.theme.appBackgroundGradient
import com.example.project.ui.theme.ProjectTheme // Reemplaza con el nombre de tu tema



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    // --- PASO 1: Cambiamos los parámetros ---
    onNavigateToRegister: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_warehouse_24),
                        contentDescription = "Logo",
                        modifier = Modifier.height(32.dp),
                        contentScale = ContentScale.Fit
                    )
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

            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ... (Ilustración y Títulos se quedan igual) ...

            Image(
                painter = painterResource(id = R.drawable.ou),
                contentDescription = "Ilustración de Bienvenida",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Fit
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(Dimens.SpacerSmall))
                Text(
                    text = "Encuentra tu próximo lugar ideal para vivir cerca de tu universidad.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }


            // --- 3. BOTONES DE ACCIÓN (AQUÍ ESTÁ EL CAMBIO) ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón principal (usando tu componente)
                PrimaryButton(
                    text = "Crear una cuenta",
                    // --- PASO 2: Usamos la nueva lambda ---
                    onClick = onNavigateToRegister
                )

                Spacer(modifier = Modifier.height(Dimens.SpacerSmall))

                // Link secundario
                TextButton(
                    // --- PASO 3: Usamos la nueva lambda ---
                    onClick = onNavigateToLogin
                ) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = UptelBlue
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
                    Text(
                        text = "Ya tengo cuenta",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = UptelBlue
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
        }
    }
}