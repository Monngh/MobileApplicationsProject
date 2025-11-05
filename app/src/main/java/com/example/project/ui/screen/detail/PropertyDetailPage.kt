package com.example.project.ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.R
import com.example.project.ui.components.PrimaryButton
import com.example.project.ui.components.SecondaryButton
import com.example.project.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PropertyDetailPage(
    onNavigateBack: () -> Unit = {},
    onCallOwner: () -> Unit = {},
    onMessageOwner: () -> Unit = {}
) {
    Scaffold(
        // --- 1. BARRA SUPERIOR (CON NAVEGACIÓN "BACK") ---
        topBar = {
            TopAppBar(
                title = {
                    TextButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
                        Text(
                            "Volver a propiedades",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                },
                // Mantenemos el logo
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_warehouse_24),
                        contentDescription = "Logo UPTEL",
                        modifier = Modifier.height(32.dp).padding(end = Dimens.PaddingMedium),
                        contentScale = ContentScale.Fit
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        // --- 2. BARRA INFERIOR (CON ACCIONES) ---
        bottomBar = {
            BottomActionsBar(
                onSave = { /* ... */ },
                onShare = { /* ... */ }
            )
        }
    ) { paddingValues ->
        // --- 3. CONTENIDO PRINCIPAL (CON SCROLL) ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(appBackgroundGradient)
                .padding(paddingValues) // Padding de la TopBar y BottomBar
                .verticalScroll(rememberScrollState())
        ) {

            // --- Galería de Imágenes ---
            ImageGallery()

            // --- Contenido de la página ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.PaddingMedium) // Padding lateral consistente
            ) {

                // --- Bloque de Título y Precio ---
                TitleBlock(
                    title = "Departamento Moderno cerca de UPP",
                    price = "$ 5,500",
                    address = "Calle Universidad 123",
                    distance = "A 50m de UPP"
                )

                Spacer(modifier = Modifier.height(Dimens.SpacerLarge))
                Divider()
                Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

                // --- Bloque de Especificaciones (Habitaciones, Baños...) ---
                SpecsBlock()

                Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

                // --- Bloque de Descripción ---
                DescriptionBlock(
                    text = "Amplio departamento moderno a solo 500 metros de la Universidad Politécnica. Ideal para estudiantes. Cuenta con sala, comedor, cocina equipada, 2 habitaciones y 1 baño. El edificio ofrece seguridad 24/7, áreas común y estacionamiento."
                )

                Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

                // --- Bloque de Características (2 columnas) ---
                CharacteristicsBlock()

                Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

                // --- Bloque de Ubicación (Mapa) ---
                LocationBlock()

                Spacer(modifier = Modifier.height(Dimens.SpacerLarge))

                // --- Tarjeta del Propietario ---
                OwnerCard(
                    name = "Carlos Rodríguez",
                    onCall = onCallOwner,
                    onMessage = onMessageOwner
                )

                Spacer(modifier = Modifier.height(Dimens.PaddingMedium)) // Padding final
            }
        }
    }
}


// --- COMPONENTES INTERNOS DE LA PÁGINA ---

@Composable
private fun ImageGallery() {
    Column {
        Image(
            painter = painterResource(id = R.drawable.placeholder_property), // Imagen principal
            contentDescription = "Foto principal",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(Dimens.SpacerSmall))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PaddingMedium),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacerSmall)
        ) {
            Image(
                painter = painterResource(id = R.drawable.placeholder_property), // Thumbnail 1
                contentDescription = "Foto 2",
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .clip(RoundedCornerShape(Dimens.CornerRadius)),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painterResource(id = R.drawable.placeholder_property), // Thumbnail 2
                contentDescription = "Foto 3",
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .clip(RoundedCornerShape(Dimens.CornerRadius)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun TitleBlock(title: String, price: String, address: String, distance: String) {
    Column {
        Spacer(modifier = Modifier.height(Dimens.SpacerLarge))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = price,
                style = MaterialTheme.typography.headlineMedium,
                color = UptelBlue,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(Dimens.SpacerSmall))
        Text(
            text = address,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Text(
            text = distance,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

@Composable
private fun SpecsBlock() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        SpecItem(icon = Icons.Default.Bed, label = "2 Habitaciones")
        SpecItem(icon = Icons.Default.Bathtub, label = "1 Baños")
        SpecItem(icon = Icons.Default.SquareFoot, label = "85 m²")
    }
}

@Composable
private fun SpecItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, tint = UptelBlue, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(Dimens.SpacerSmall))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun DescriptionBlock(text: String) {
    Column {
        Text(
            text = "Descripción",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun CharacteristicsBlock() {
    Column {
        Text(
            text = "Características",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
        Row(modifier = Modifier.fillMaxWidth()) {
            // Columna 1
            Column(modifier = Modifier.weight(1f)) {
                CharacteristicItem("Cocina equipada")
                CharacteristicItem("Agua caliente")
                CharacteristicItem("Estacionamiento")
                CharacteristicItem("Amueblado")
            }
            // Columna 2
            Column(modifier = Modifier.weight(1f)) {
                CharacteristicItem("Internet de alta velocidad")
                CharacteristicItem("Seguridad 24/7")
                CharacteristicItem("Área de lavado")
                CharacteristicItem("Closets amplios")
            }
        }
    }
}

@Composable
private fun CharacteristicItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = Dimens.SpacerSmall)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_warehouse_24), // Placeholder (un punto)
            contentDescription = null,
            tint = UptelBlue,
            modifier = Modifier.size(8.dp)
        )
        Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

@Composable
private fun LocationBlock() {
    Column {
        Text(
            text = "Ubicación",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
        // Placeholder del mapa
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(Dimens.CornerRadius))
                .background(LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Mapa de ubicación", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }
    }
}

@Composable
private fun OwnerCard(name: String, onCall: () -> Unit, onMessage: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.CardElevation),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ou), // Placeholder
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(Dimens.SpacerMedium))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Propietario",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
            // Iconos de contacto
            IconButton(onClick = onCall) {
                Icon(Icons.Default.Phone, contentDescription = "Llamar", tint = UptelBlue)
            }
            IconButton(onClick = onMessage) {
                Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Enviar Mensaje", tint = UptelBlue)
            }
        }
    }
}

@Composable
private fun BottomActionsBar(onSave: () -> Unit, onShare: () -> Unit) {
    BottomAppBar(
        containerColor = Color.White,
        tonalElevation = Dimens.CardElevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PaddingMedium),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(onClick = onShare) {
                Icon(Icons.Default.Share, contentDescription = "Compartir", tint = TextPrimary)
                Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
                Text("Compartir", color = TextPrimary)
            }
        }
    }
}