package com.example.project.ui.screen.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project.ui.theme.Dimens
import com.example.project.R
import com.example.project.ui.components.PrimaryButton
import com.example.project.ui.components.PropertyCard
import com.example.project.ui.theme.ProjectTheme
import com.example.project.ui.theme.UptelBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    paddingValues: PaddingValues,
    onNavigateToPropertyDetail: (String) -> Unit // Nuevo para navegar al detalle
) {
    // Estado para el campo de búsqueda
    var searchText by rememberSaveable { mutableStateOf("") }

    // Usamos LazyColumn para la estructura general
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background), // Fondo de tu tema
        contentPadding = PaddingValues(vertical = Dimens.PaddingMedium)
    ) {

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.PaddingMedium) // Usa tus Dimens
            ) {
                // Lo dividimos en dos líneas para igualar el diseño
                Text(
                    text = "Descubre tu",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Nuevo Hogar",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
        }
        // --- Barra de Búsqueda Moderna ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.PaddingMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Buscar lugar...") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp) // Altura fija
                        .clip(RoundedCornerShape(Dimens.CornerRadius)),
                    colors = TextFieldDefaults.colors(
                        // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
                        // 'containerColor' se divide por estados:
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),

                        // Y mantenemos los indicadores transparentes
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(Dimens.CornerRadius)
                )
                Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
                IconButton(
                    onClick = { /* TODO: Abrir filtros */ },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(Dimens.CornerRadius))
                        .background(UptelBlue.copy(alpha = 0.1f)) // Tu color con transparencia
                ) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Filtros",
                        tint = UptelBlue // Tu color
                    )
                }
            }
        }

        // Espacio
        item {
            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
        }

        // --- Categorías (Chips modernos) ---
        item {
            CategorySection(onCategorySelected = { category ->
                // TODO: Implementar lógica de filtrado
                println("Categoría seleccionada: $category")
            })
        }

        // Espacio
        item {
            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
        }

        // --- Propiedades Destacadas (Carrusel) ---
        item {
            Text(
                text = "Propiedades Destacadas",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = Dimens.PaddingMedium)
            )
            Spacer(modifier = Modifier.height(Dimens.SpacerMedium))

            LazyRow(
                contentPadding = PaddingValues(horizontal = Dimens.PaddingMedium),
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
            ) {
                items(3) { index ->
                    PropertyCard(
                        modifier = Modifier.width(300.dp),
                        imageUrl = R.drawable.placeholder_property, // Usa tu placeholder
                        title = "Estudio Acogedor Cerca de UPP $index",
                        location = "Centro, Puebla",
                        price = "450",
                        rating = 4.5f,
                        beds = 1,
                        baths = 1,
                        isVerified = true,
                        isFavorite = (index == 0),
                        onCardClick = { onNavigateToPropertyDetail("propertyId_${index}") },
                        onFavoriteClick = {}
                    )
                }
            }
        }

        // Espacio
        item {
            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
        }

        // --- Todas las Propiedades (Cuadrícula Moderna) ---
        item {
            Text(
                text = "Todas las Propiedades",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = Dimens.PaddingMedium)
            )
            Spacer(modifier = Modifier.height(Dimens.SpacerMedium))
        }

        // Usamos LazyVerticalGrid dentro de LazyColumn para la cuadrícula
        // Esto es un patrón avanzado, pero es necesario si quieres que la cuadrícula se desplace con el resto.
        // Si la cuadrícula fuera la única cosa que se desplaza, usarías LazyVerticalGrid directamente.
        item {
            // Este box con altura fija es crucial cuando anidas LazyVerticalGrid en LazyColumn
            Box(modifier = Modifier.height(600.dp)) { // <--- Ajusta esta altura según cuántas filas quieres ver
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Dos columnas
                    contentPadding = PaddingValues(horizontal = Dimens.PaddingMedium, vertical = Dimens.PaddingSmall),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
                    verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
                ) {
                    items(6) { index ->
                        PropertyCard(
                            imageUrl = R.drawable.placeholder_property, // Usa tu placeholder
                            title = "Casa Bonita $index",
                            location = "Cholula, Puebla",
                            price = "320",
                            rating = 4.0f,
                            beds = 2,
                            baths = 1,
                            isVerified = false,
                            isFavorite = (index % 2 == 0),
                            onCardClick = { onNavigateToPropertyDetail("propertyId_grid_${index}") },
                            onFavoriteClick = {}
                        )
                    }
                }
            }
        }
    }
}

// Composable de ayuda para las categorías (actualizado para un look moderno)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySection(onCategorySelected: (String) -> Unit) {
    var selectedCategoryIndex by remember { mutableStateOf(0) } // Por defecto el primero seleccionado
    val categories = listOf("Rental House", "Apartment", "Houses", "Rooms", "Lofts") // Nuevas categorías

    Column(modifier = Modifier.padding(horizontal = Dimens.PaddingMedium)) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacerSmall)
        ) {
            itemsIndexed(categories) { index, category ->
                FilterChip(
                    selected = (selectedCategoryIndex == index),
                    onClick = {
                        selectedCategoryIndex = index // Siempre seleccionamos uno al hacer clic
                        onCategorySelected(category)
                    },
                    label = { Text(category, style = MaterialTheme.typography.labelMedium) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.Transparent, // Fondo transparente por defecto
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedContainerColor = UptelBlue, // Tu color principal
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        selected = (selectedCategoryIndex == index),
                        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        selectedBorderColor = UptelBlue,
                        borderWidth = 1.dp,
                        enabled = true // <-- ¡AÑADE ESTA LÍNEA!
                    ),
                    shape = RoundedCornerShape(Dimens.CornerRadius) // Tus esquinas redondeadas
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    ProjectTheme {
        HomePage(paddingValues = PaddingValues(0.dp), onNavigateToPropertyDetail = {})
    }
}