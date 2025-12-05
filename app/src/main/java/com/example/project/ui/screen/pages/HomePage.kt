package com.example.project.ui.screen.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.project.domain.model.Property
import com.example.project.ui.screen.pages.home.HomeViewModel
import com.example.project.ui.screen.pages.home.HomeViewModelFactory
import com.example.project.ui.screen.pages.home.HomeUiState

// Modelo para los filtros de características
data class FeatureFilter(
    val label: String,
    val type: FilterType,
    val value: Int
)

enum class FilterType {
    BEDROOMS,
    BATHROOMS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    paddingValues: PaddingValues,
    onNavigateToPropertyDetail: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context))
    val uiState by viewModel.uiState.collectAsState()

    var searchText by remember { mutableStateOf("") }
    var selectedFilters by remember { mutableStateOf(setOf<FeatureFilter>()) }

    // Filtros de características disponibles
    val featureFilters = remember {
        listOf(
            FeatureFilter("1 Hab", FilterType.BEDROOMS, 1),
            FeatureFilter("2 Hab", FilterType.BEDROOMS, 2),
            FeatureFilter("3+ Hab", FilterType.BEDROOMS, 3),
            FeatureFilter("1 Baño", FilterType.BATHROOMS, 1),
            FeatureFilter("2 Baños", FilterType.BATHROOMS, 2),
            FeatureFilter("3+ Baños", FilterType.BATHROOMS, 3)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // HEADER
            HeaderSection(searchText = searchText, onSearchChange = { searchText = it })

            // CONTENIDO
            when (uiState) {
                is HomeUiState.Idle -> {
                    LoadingSection()
                }
                is HomeUiState.Loading -> {
                    LoadingSection()
                }
                is HomeUiState.Success -> {
                    val allProperties = (uiState as HomeUiState.Success).properties
                    
                    // Filtrar propiedades por búsqueda y características
                    val filteredProperties = allProperties.filter { property ->
                        // Filtro por texto de búsqueda (título)
                        val matchesSearch = searchText.isBlank() || 
                            property.title.contains(searchText, ignoreCase = true)
                        
                        // Filtro por características seleccionadas
                        val matchesFilters = if (selectedFilters.isEmpty()) {
                            true
                        } else {
                            selectedFilters.all { filter ->
                                when (filter.type) {
                                    FilterType.BEDROOMS -> {
                                        if (filter.value >= 3) {
                                            property.bedrooms >= 3
                                        } else {
                                            property.bedrooms == filter.value
                                        }
                                    }
                                    FilterType.BATHROOMS -> {
                                        if (filter.value >= 3) {
                                            property.bathrooms >= 3
                                        } else {
                                            property.bathrooms == filter.value
                                        }
                                    }
                                }
                            }
                        }
                        
                        matchesSearch && matchesFilters
                    }
                    
                    PropertiesListSection(
                        properties = filteredProperties,
                        featureFilters = featureFilters,
                        selectedFilters = selectedFilters,
                        onFilterToggle = { filter ->
                            selectedFilters = if (selectedFilters.contains(filter)) {
                                selectedFilters - filter
                            } else {
                                // Si es del mismo tipo, reemplazar (solo uno por tipo)
                                val filtered = selectedFilters.filter { it.type != filter.type }
                                filtered.toSet() + filter
                            }
                        },
                        onClearFilters = { selectedFilters = emptySet() },
                        onPropertyClick = onNavigateToPropertyDetail
                    )
                }
                is HomeUiState.Error -> {
                    ErrorSection(
                        error = (uiState as HomeUiState.Error).message,
                        onRetry = { viewModel.loadProperties() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeaderSection(
    searchText: String,
    onSearchChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "¡Bienvenido a DOMY!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Tu hogar universitario te espera",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                }
            }

            // Barra de búsqueda funcional
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                    TextField(
                        value = searchText,
                        onValueChange = onSearchChange,
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Buscar por título...") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                    if (searchText.isNotBlank()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Limpiar",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PropertiesListSection(
    properties: List<Property>,
    featureFilters: List<FeatureFilter>,
    selectedFilters: Set<FeatureFilter>,
    onFilterToggle: (FeatureFilter) -> Unit,
    onClearFilters: () -> Unit,
    onPropertyClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Sección de Características (Filtros)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Características",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                if (selectedFilters.isNotEmpty()) {
                    TextButton(onClick = onClearFilters) {
                        Text("Limpiar", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                featureFilters.forEach { filter ->
                    val isSelected = selectedFilters.contains(filter)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onFilterToggle(filter) },
                        label = { 
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = when (filter.type) {
                                        FilterType.BEDROOMS -> Icons.Default.Bed
                                        FilterType.BATHROOMS -> Icons.Default.Bathtub
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(filter.label)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White
                        )
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        // Título de propiedades con contador
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Propiedades disponibles",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        "${properties.size} resultados",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        // Mensaje cuando no hay resultados
        if (properties.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "No se encontraron propiedades",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            "Intenta con otros filtros",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }

        // Lista de propiedades
        items(properties) { property ->
            PropertyCard(
                property = property,
                onClick = { onPropertyClick(property.id.toString()) }
            )
        }
    }
}

@Composable
private fun PropertyCard(
    property: Property,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column {
            // Imagen de la propiedad
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (!property.mainImageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(property.mainImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = property.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder cuando no hay imagen
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                }
            }

            // Contenido
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    property.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
                        "$${property.price}/mes",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bed, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("${property.bedrooms}", style = MaterialTheme.typography.bodySmall)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bathtub, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("${property.bathrooms}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
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
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(16.dp))
            Text("Error", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(error, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}
