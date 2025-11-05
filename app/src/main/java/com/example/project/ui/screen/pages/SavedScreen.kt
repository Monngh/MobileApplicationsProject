package com.example.project.ui.screen.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.project.R
import com.example.project.ui.components.FavoriteItemCard // <-- Tu nueva tarjeta
import com.example.project.ui.components.PrimaryButton
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.TextSecondary
import com.example.project.ui.theme.UptelBlue

// --- Modelo de datos de ejemplo (reemplazar con datos reales) ---
data class FavoriteItem(
    val id: String,
    val title: String,
    val location: String,
    val price: String,
    val imageUrl: Int
)

// --- Datos de ejemplo ---
private val sampleFavorites = listOf(
    FavoriteItem("1", "Estudio Acogedor", "Centro, Puebla", "450", R.drawable.placeholder_property),
    FavoriteItem("2", "Apartamento Moderno", "Cholula, Puebla", "620", R.drawable.placeholder_property),
    FavoriteItem("3", "Casa Bonita", "Valle de las Rosas", "320", R.drawable.placeholder_property)
)

@Composable
fun SavedScreen(
    paddingValues: PaddingValues,
    onNavigateToPropertyDetail: (String) -> Unit,
    onExploreClick: () -> Unit // <-- Para el botón del estado vacío
) {
    // --- ESTADO (Simulado) ---
    // Para probar el estado vacío, cambia esto a: mutableStateOf(emptyList())
    var favorites by remember { mutableStateOf(sampleFavorites) }
    var selectedFilterIndex by remember { mutableStateOf(0) }
    val filters = listOf("Recientes", "Precio (Bajo)", "Precio (Alto)")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // Respeta el padding del Scaffold
    ) {
        if (favorites.isEmpty()) {
            EmptyFavoritesView(onExploreClick = onExploreClick)
        } else {
            // --- Vista de Lista Llena ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(Dimens.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall) // Espacio entre tarjetas
            ) {
                // --- Título ---
                item {
                    Text(
                        text = "Mis Favoritos",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = Dimens.SpacerSmall)
                    )
                }

                // --- Filtros ---
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacerSmall)
                    ) {
                        itemsIndexed(filters) { index, filter ->
                            FilterChip(
                                selected = selectedFilterIndex == index,
                                onClick = { selectedFilterIndex = index },
                                label = { Text(filter) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = UptelBlue,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // --- Espacio ---
                item {
                    Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
                }

                // --- Lista de Tarjetas ---
                items(favorites) { favorite ->
                    FavoriteItemCard(
                        imageUrl = favorite.imageUrl,
                        title = favorite.title,
                        location = favorite.location,
                        price = favorite.price,
                        onCardClick = { onNavigateToPropertyDetail(favorite.id) },
                        onFavoriteClick = {
                            // Lógica para remover de favoritos
                            favorites = favorites.filterNot { it.id == favorite.id }
                        }
                    )
                }
            }
        }
    }
}

// --- Estado Vacío Hermoso ---
@Composable
private fun EmptyFavoritesView(
    modifier: Modifier = Modifier,
    onExploreClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.PaddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "Sin favoritos",
            tint = UptelBlue.copy(alpha = 0.5f), // Tu color principal, semitransparente
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
        Text(
            text = "Aún no tienes favoritos",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(Dimens.SpacerSmall))
        Text(
            text = "Toca el ícono del corazón en cualquier propiedad para guardarla aquí.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
        PrimaryButton( // <-- Tu botón reutilizable
            text = "Explorar Propiedades",
            onClick = onExploreClick
        )
    }
}