package com.example.project.ui.screen.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.project.R // Necesitarás un avatar
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.TextSecondary
import com.example.project.ui.theme.UptelBlue

// --- Modelo de datos de ejemplo ---
data class ChatPreview(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int,
    val avatarUrl: Int // Usamos Int para R.drawable
)

// --- Datos de ejemplo ---
val sampleChats = listOf(
    ChatPreview("1", "Julian Dasilva", "Hi Julian! See you after work?", "12:00", 2, R.drawable.profile_avatar_placeholder),
    ChatPreview("2", "Mike Lyne", "I must tell you my interview this...", "13:50", 1, R.drawable.profile_avatar_placeholder),
    ChatPreview("3", "Claire Kumas", "Yes I can do this to you in the...", "13:30", 0, R.drawable.profile_avatar_placeholder),
    ChatPreview("4", "Molly Clark", "Yes I am so happy! 😊", "12:45", 0, R.drawable.profile_avatar_placeholder)
)

@Composable
fun MessagesScreen(
    paddingValues: PaddingValues,
    onNavigateToChatDetail: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val chats = sampleChats // <-- Reemplazar con datos reales

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // <-- Respeta el padding del Scaffold
            .padding(horizontal = Dimens.PaddingMedium)
    ) {
        // --- Barra de Búsqueda ---
        item {
            Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.CornerRadius)
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
        }

        // --- Avatares "Online" ---
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacerMedium)
            ) {
                items(chats) { chat ->
                    Image(
                        painter = painterResource(id = chat.avatarUrl),
                        contentDescription = chat.name,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
        }

        // --- Lista de Chats ---
        items(chats) { chat ->
            MessagePreviewRow(
                chat = chat,
                onClick = { onNavigateToChatDetail(chat.id) }
            )
            Divider(modifier = Modifier.padding(vertical = Dimens.SpacerMedium))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagePreviewRow(
    chat: ChatPreview,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = chat.avatarUrl),
            contentDescription = chat.name,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(Dimens.SpacerMedium))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = chat.lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary, // Tu color
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = chat.timestamp,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(Dimens.SpacerXSmall))
            if (chat.unreadCount > 0) {
                Badge(
                    containerColor = UptelBlue // Tu color
                ) {
                    Text(text = chat.unreadCount.toString())
                }
            }
        }
    }
}