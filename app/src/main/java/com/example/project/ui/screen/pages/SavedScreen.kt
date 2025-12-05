package com.example.project.ui.screen.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project.R
import com.example.project.domain.model.Property
import com.example.project.ui.screen.pages.saved.SavedViewModel
import com.example.project.ui.screen.pages.saved.SavedViewModelFactory
import com.example.project.ui.screen.pages.saved.SavedUiState

@Composable
fun SavedScreen(
    paddingValues: PaddingValues,
    onNavigateToPropertyDetail: (String) -> Unit,
    onExploreClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: SavedViewModel = viewModel(factory = SavedViewModelFactory(context))
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        when (uiState) {
            is SavedUiState.Idle -> {
                LoadingSection()
            }
            is SavedUiState.Loading -> {
                LoadingSection()
            }
            is SavedUiState.Success -> {
                val favorites = (uiState as SavedUiState.Success).favorites
                if (favorites.isEmpty()) {
                    EmptyFavoritesState(onExploreClick = onExploreClick)
                } else {
                    FavoritesListSection(
                        favorites = favorites,
                        onNavigateToPropertyDetail = onNavigateToPropertyDetail,
                        onRemoveFromFavorites = { viewModel.removeFromFavorites(it) }
                    )
                }
            }
            is SavedUiState.Error -> {
                ErrorSection(
                    error = (uiState as SavedUiState.Error).message,
                    onRetry = { viewModel.loadFavorites() }
                )
            }
        }
    }
}

@Composable
private fun FavoritesListSection(
    favorites: List<Property>,
    onNavigateToPropertyDetail: (String) -> Unit,
    onRemoveFromFavorites: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    "Mis Favoritos",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${favorites.size} propiedades guardadas",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
            }
        }

        // Lista
        items(favorites) { property ->
            PropertyFavoriteCard(
                property = property,
                onClick = { onNavigateToPropertyDetail(property.id.toString()) },
                onRemove = { onRemoveFromFavorites(property.id.toString()) }
            )
        }
    }
}

@Composable
private fun PropertyFavoriteCard(
    property: Property,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick
    ) {
        Column {
            // Imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.casa),
                    contentDescription = property.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradiente
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Botón favorito
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Quitar de favoritos",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Contenido
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    property.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${property.city}, ${property.state}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "$${property.price.toInt()}/mes",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FeatureChip(Icons.Outlined.Bed, "${property.bedrooms}")
                        FeatureChip(Icons.Outlined.Bathtub, "${property.bathrooms}")
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            icon,
            null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun EmptyFavoritesState(onExploreClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Card(
                modifier = Modifier
                    .size(120.dp)
                    .shadow(12.dp, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Text(
                "No tienes favoritos aún",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            Text(
                "Explora propiedades y guarda tus favoritas",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onExploreClick,
                modifier = Modifier.width(200.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Explorar Propiedades")
            }
        }
    }
}

@Composable
private fun LoadingSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorSection(error: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                Icons.Default.Warning,
                null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("Error", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(error, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}
