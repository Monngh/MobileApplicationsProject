package com.example.project.ui.screen.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project.data.remote.dto.NotificationDTO
import com.example.project.ui.screen.pages.notifications.NotificationsViewModel
import com.example.project.ui.screen.pages.notifications.NotificationsViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPage(
    onOpenDrawer: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: NotificationsViewModel = viewModel(factory = NotificationsViewModelFactory(context))
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
    ) {
        // Fondo decorativo sutil
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
        )

        Scaffold(
            topBar = {
                NotificationHeader(
                    unreadCount = uiState.unreadCount,
                    onMarkAllAsRead = { viewModel.markAllAsRead() },
                    onOpenDrawer = onOpenDrawer
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(uiState.error ?: "Error")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadNotifications() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                uiState.notifications.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.NotificationsNone,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No tienes notificaciones",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
                else -> {
                    val unreadNotifications = uiState.notifications.filter { !it.isRead }
                    val readNotifications = uiState.notifications.filter { it.isRead }
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(8.dp)) }

                        // --- SECCIÓN "NO LEÍDAS" ---
                        if (unreadNotifications.isNotEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .shadow(
                                            elevation = 4.dp,
                                            shape = RoundedCornerShape(16.dp),
                                            clip = true
                                        ),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFE3F2FD)
                                    )
                                ) {
                                    Column {
                                        unreadNotifications.forEachIndexed { index, notification ->
                                            NotificationItemElegante(
                                                notification = notification,
                                                isUnread = true,
                                                onMarkAsRead = { viewModel.markAsRead(notification.id) },
                                                onDelete = { viewModel.deleteNotification(notification.id) }
                                            )
                                            if (index < unreadNotifications.size - 1) {
                                                HorizontalDivider(
                                                    modifier = Modifier.padding(horizontal = 16.dp),
                                                    color = Color(0xFFBBDEFB)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            item { Spacer(modifier = Modifier.height(20.dp)) }
                        }

                        // --- SECCIÓN "LEÍDAS" ---
                        if (readNotifications.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Anteriores",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    ),
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                                )
                            }
                        }

                        items(readNotifications) { notification ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                    .shadow(
                                        elevation = 2.dp,
                                        shape = RoundedCornerShape(16.dp),
                                        clip = true
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                NotificationItemElegante(
                                    notification = notification,
                                    isUnread = false,
                                    onMarkAsRead = { },
                                    onDelete = { viewModel.deleteNotification(notification.id) }
                                )
                            }
                        }

                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationHeader(
    unreadCount: Int,
    onMarkAllAsRead: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                CircleShape
                            )
                            .size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Notificaciones",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        if (unreadCount > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                                )
                                Text(
                                    text = "$unreadCount no leídas",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                )
                            }
                        }
                    }
                }

                if (unreadCount > 0) {
                    TextButton(
                        onClick = onMarkAllAsRead,
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text(
                            text = "Marcar todas",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Marcar todas",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun NotificationItemElegante(
    notification: NotificationDTO,
    isUnread: Boolean,
    onMarkAsRead: () -> Unit,
    onDelete: () -> Unit
) {
    // Mapear tipo a icono
    val icon: ImageVector = when (notification.type) {
        "message" -> Icons.Default.ChatBubble
        "property" -> Icons.Default.Home
        "verification" -> Icons.Default.CheckCircle
        "reminder" -> Icons.Default.Star
        else -> Icons.Default.Notifications
    }

    // Formatear tiempo relativo
    val timeAgo = formatTimeAgo(notification.createdAt)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Icono con fondo
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (isUnread) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    else Color(0xFFF0F4F8),
                    CircleShape
                )
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = notification.title,
                tint = if (isUnread) MaterialTheme.colorScheme.primary 
                       else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = notification.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        if (isUnread) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = notification.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = timeAgo,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                )
            }

            // Acciones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isUnread) {
                    TextButton(
                        onClick = onMarkAsRead,
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Marcar como leída",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Marcar",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

private fun formatTimeAgo(dateString: String): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = format.parse(dateString.replace(".000000Z", "")) ?: return dateString
        val now = Date()
        val diffMs = now.time - date.time
        val diffMinutes = diffMs / (1000 * 60)
        val diffHours = diffMs / (1000 * 60 * 60)
        val diffDays = diffMs / (1000 * 60 * 60 * 24)

        when {
            diffMinutes < 1 -> "Ahora"
            diffMinutes < 60 -> "Hace ${diffMinutes}m"
            diffHours < 24 -> "Hace ${diffHours}h"
            diffDays < 7 -> "Hace ${diffDays}d"
            else -> SimpleDateFormat("dd/MM", Locale.getDefault()).format(date)
        }
    } catch (e: Exception) {
        dateString
    }
}