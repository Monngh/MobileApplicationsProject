package com.example.project.ui.screen.chat_detail

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.project.R // Necesitarás un avatar de placeholder
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.TextSecondary
import com.example.project.ui.theme.UptelBlue

// --- Modelo de datos de ejemplo ---
data class Message(
    val id: String,
    val text: String,
    val timestamp: String,
    val isSentByMe: Boolean
)

// --- Datos de ejemplo (reemplazar con datos reales) ---
val sampleMessages = listOf(
    Message("1", "Hi Ankur! What's Up?", "Yesterday 14:26 PM", false),
    Message("2", "Oh, hello! All perfectly fine I'm just heading out for something.", "Yesterday 14:38 PM", true),
    Message("3", "Yeah sure I'll be there this weekend with my brother.", "Yesterday 14:44 PM", false),
    Message("4", "Yes I Am So Happy 😊", "Yesterday 14:45 PM", true)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    chatId: String, // Para saber de qué chat cargar mensajes
    onNavigateBack: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    val messages = sampleMessages // <-- Cargarías esto desde tu ViewModel

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.profile_avatar_placeholder), // Reemplazar
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
                        Column {
                            Text(
                                text = "Molly Clark", // <-- Nombre del chat (vendría con chatId)
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Online",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary // Tu color
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Llamar */ }) {
                        Icon(Icons.Default.Call, contentDescription = "Llamar")
                    }
                    IconButton(onClick = { /* TODO: VideoLlamar */ }) {
                        Icon(Icons.Default.Videocam, contentDescription = "Videollamada")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            ChatInputBar(
                text = messageText,
                onTextChange = { messageText = it },
                onSendClick = {
                    // TODO: Lógica de envío
                    messageText = ""
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                .padding(paddingValues)
                .padding(horizontal = Dimens.PaddingMedium),
            reverseLayout = true // Los chats crecen de abajo hacia arriba
        ) {
            items(messages.reversed()) { message ->
                ChatBubble(message = message)
                Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val alignment = if (message.isSentByMe) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (message.isSentByMe) UptelBlue else MaterialTheme.colorScheme.surface
    val textColor = if (message.isSentByMe) Color.White else MaterialTheme.colorScheme.onSurface
    val shape = if (message.isSentByMe) {
        RoundedCornerShape(Dimens.CornerRadius, Dimens.CornerRadius, 0.dp, Dimens.CornerRadius)
    } else {
        RoundedCornerShape(Dimens.CornerRadius, Dimens.CornerRadius, Dimens.CornerRadius, 0.dp)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .clip(shape)
                .background(backgroundColor)
                .padding(Dimens.PaddingSmall),
            horizontalAlignment = if (message.isSentByMe) Alignment.End else Alignment.Start
        ) {
            Text(text = message.text, color = textColor)
            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(Dimens.PaddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* TODO: Adjuntar */ }) {
            Icon(Icons.Default.Add, contentDescription = "Adjuntar")
        }
        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text("Type Your Message") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(Dimens.CornerRadius),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        IconButton(
            onClick = onSendClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(UptelBlue)
        ) {
            Icon(Icons.Default.Send, contentDescription = "Enviar", tint = Color.White)
        }
    }
}