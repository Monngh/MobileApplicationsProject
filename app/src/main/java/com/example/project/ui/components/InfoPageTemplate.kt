package com.example.project.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.project.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoPageTemplate(
    title: String,
    onOpenDrawer: () -> Unit,
    // Este parámetro nos permite "inyectar" el contenido
    // específico de cada página (texto, imágenes, etc.)
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                },
                // Un color diferente para distinguirlas de la app principal
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { paddingValues ->
        // Contenido con scroll y padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(Dimens.PaddingMedium), // Usamos tus Dimens
            content = content // Aquí se inyecta tu contenido
        )
    }
}