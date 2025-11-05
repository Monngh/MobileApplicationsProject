package com.example.project.ui.screen.main.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project.R
import com.example.project.ui.theme.*


// --- 1. DATA CLASS PARA EL DISEÑO ---
// Un modelo de datos solo para visualizar el diseño
data class Notification(
    val id: Int,
    val icon: ImageVector,
    val title: String,
    val timestamp: String,
    val description: String,
    val isUnread: Boolean = false
)

// --- 2. DATOS DE EJEMPLO (PURO DISEÑO) ---
val unreadNotifications = listOf(
    Notification(
        id = 1,
        icon = Icons.Default.ChatBubble,
        title = "Nuevo mensaje",
        timestamp = "Hace 2 horas",
        description = "Carlos Rodríguez te ha enviado un mensaje sobre \"Departamento Moderno\"",
        isUnread = true
    ),
    Notification(
        id = 2,
        icon = Icons.Default.Home,
        title = "Nueva propiedad disponible",
        timestamp = "Hace 1 día",
        description = "Se ha publicado una nueva propiedad que coincide con tus preferencias cerca de UPP",
        isUnread = true
    )
)

val readNotifications = listOf(
    Notification(
        id = 3,
        icon = Icons.Default.CheckCircle,
        title = "Verificación completada",
        timestamp = "Hace 3 días",
        description = "Tu cuenta ha sido verificada correctamente"
    ),
    Notification(
        id = 4,
        icon = Icons.Default.ChatBubble,
        title = "Respuesta recibida",
        timestamp = "Hace 5 días",
        description = "Ana López ha respondido a tu consulta sobre el \"Estudio Amueblado\""
    ),
    Notification(
        id = 5,
        icon = Icons.Default.Star,
        title = "Recordatorio de visita",
        timestamp = "Hace 1 semana",
        description = "Tienes programada una visita para mañana a las 15:00 en \"Apartamento Moderno 2BR\""
    )
)


// --- 3. PANTALLA DE NOTIFICACIONES ---
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NotificationPage(
    onOpenDrawer: () -> Unit = {}
) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(appBackgroundGradient)
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = Dimens.SpacerLarge)
        ) {

            // --- ENCABEZADO DE LA PANTALLA ---
            item {
                NotificationHeader(
                    unreadCount = unreadNotifications.size,
                    onMarkAllAsRead = { /* ... */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.SpacerLarge))
            }

            // --- SECCIÓN "NO LEÍDAS" ---
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.PaddingMedium),
                    shape = RoundedCornerShape(Dimens.CornerRadius),
                    elevation = CardDefaults.cardElevation(defaultElevation = Dimens.CardElevation / 2),
                    // --- ¡CAMBIO AQUÍ! ---
                    // Usamos el nuevo color "gris claro" (azul pálido)
                    colors = CardDefaults.cardColors(containerColor = UnreadNotificationBg)
                ) {
                    Column {
                        unreadNotifications.forEachIndexed { index, notification ->
                            // Le decimos al item que NO está leído (isUnread = true)
                            NotificationItem(notification = notification, isUnread = true)
                            if (index < unreadNotifications.size - 1) {
                                Divider(modifier = Modifier.padding(horizontal = Dimens.PaddingMedium))
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.SpacerLarge))
            }

            // --- SECCIÓN "LEÍDAS" ---
            items(readNotifications) { notification ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.PaddingMedium),
                    shape = RoundedCornerShape(Dimens.CornerRadius),
                    elevation = CardDefaults.cardElevation(defaultElevation = Dimens.CardElevation / 2),
                    // Estas se quedan blancas, como en el diseño
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    // Le decimos al item que SÍ está leído (isUnread = false)
                    NotificationItem(notification = notification, isUnread = false)
                }
                Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
            }
        }
    }
}
// --- 4. COMPONENTE HEADER ---
@Composable
private fun NotificationHeader(
    unreadCount: Int,
    onMarkAllAsRead: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingMedium), // Padding lateral
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // Columna de Títulos
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Notificaciones",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(Dimens.SpacerXSmall))
            Text(
                text = "Tienes $unreadCount notificaciones sin leer",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.width(Dimens.SpacerSmall))

        // Botón de Marcar Leídas
        TextButton(onClick = onMarkAllAsRead) {
            Text(
                text = "Marcar todas como leídas",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = UptelBlue
                ),
                textAlign = TextAlign.End // Alinear a la derecha por si se parte en 2 líneas
            )
        }
    }
}

// --- 5. COMPONENTE ITEM DE NOTIFICACIÓN (ACTUALIZADO) ---
@Composable
private fun NotificationItem(
    notification: Notification,
    isUnread: Boolean // <-- Nuevo parámetro
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingMedium),
        verticalAlignment = Alignment.Top
    ) {
        // Icono
        Icon(
            imageVector = notification.icon,
            contentDescription = notification.title,
            tint = UptelBlue,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(Dimens.SpacerMedium))

        // Columna de Texto
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = notification.timestamp,
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.height(Dimens.SpacerXSmall))
            Text(
                text = notification.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.width(Dimens.SpacerSmall))

        // --- ¡CAMBIO AQUÍ! ---
        // Iconos de Acción
        Row {
            // El "Check" solo aparece si la notificación NO está leída
            if (isUnread) {
                IconButton(
                    onClick = { /* Lógica de marcar leída */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Marcar como leída",
                        tint = TextSecondary
                    )
                }
            }

            // La "X" (descartar) aparece siempre
            IconButton(
                onClick = { /* Lógica de descartar */ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Descartar",
                    tint = TextSecondary
                )
            }
        }
    }
}