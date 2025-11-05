package com.example.project.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project.R
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.ProjectTheme
import com.example.project.ui.theme.TextSecondary
import com.example.project.ui.theme.UptelBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteItemCard(
    modifier: Modifier = Modifier,
    imageUrl: Int, // Placeholder
    title: String,
    location: String,
    price: String,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        onClick = onCardClick,
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp), // Altura fija para la tarjeta horizontal
        shape = MaterialTheme.shapes.medium.copy(CornerSize(Dimens.CornerRadius)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.CardElevation)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // --- Imagen ---
            Image(
                painter = painterResource(id = imageUrl),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(110.dp) // Ancho fijo para la imagen
            )

            // --- Contenido ---
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(Dimens.PaddingSmall)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(Dimens.SpacerXSmall))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacerXSmall))
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }

                // Este Spacer empuja el precio hacia abajo
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "$${price} /mes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = UptelBlue // Tu color principal
                )
            }

            // --- Botón de Favorito ---
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(Dimens.PaddingSmall),
                contentAlignment = Alignment.TopCenter // Alineado arriba
            ) {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = Icons.Filled.Favorite, // Siempre lleno en la pantalla de favoritos
                        contentDescription = "Quitar de favoritos",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FavoriteItemCardPreview() {
    ProjectTheme {
        FavoriteItemCard(
            imageUrl = R.drawable.placeholder_property,
            title = "Estudio Moderno",
            location = "Centro, Puebla",
            price = "500",
            onCardClick = {},
            onFavoriteClick = {}
        )
    }
}