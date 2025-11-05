package com.example.project.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.project.ui.theme.ProjectTheme
import com.example.project.ui.theme.UptelBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyCard(
    modifier: Modifier = Modifier,
    imageUrl: Int, // Cambiado a Int para recursos locales por ahora
    title: String,
    location: String, // Cambiado de 'distance' a 'location' para ser más descriptivo
    price: String,
    rating: Float, // Nuevo: para la calificación
    beds: Int,     // Nuevo: número de camas
    baths: Int,    // Nuevo: número de baños
    isVerified: Boolean, // Nuevo: ícono de verificación
    isFavorite: Boolean,
    onCardClick: () -> Unit, // Para navegar al detalle
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp) // Mayor altura para el nuevo diseño
            .clip(RoundedCornerShape(Dimens.CornerRadius)), // Usamos tu CornerRadius
        onClick = onCardClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // --- IMAGEN DE FONDO ---
            Image(
                painter = painterResource(id = imageUrl),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // --- Degradado Oscuro en la parte inferior para que el texto se lea mejor ---
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                            startY = 300f
                        )
                    )
            )

            // --- CONTENIDO SUPERIOR (Verificado, Precio, Favorito) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.PaddingSmall)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Verificado y Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isVerified) {
                            Icon(
                                imageVector = Icons.Filled.Verified,
                                contentDescription = "Verificado",
                                tint = UptelBlue, // Tu color para verificado
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(Dimens.SpacerXSmall))
                        }
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFC107), // Amarillo para estrellas
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimens.SpacerXSmall))
                        Text(
                            text = rating.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Precio y Favorito
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$${price} /mes",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(Dimens.SpacerSmall))
                        IconButton(
                            onClick = onFavoriteClick,
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(Dimens.CornerRadius/2))
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (isFavorite) Color.Red else Color.White
                            )
                        }
                    }
                }
            }


            // --- CONTENIDO INFERIOR (Título y Ubicación) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(Dimens.PaddingMedium)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(Dimens.SpacerXSmall))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacerXSmall))
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                Spacer(modifier = Modifier.height(Dimens.SpacerSmall))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bed,
                        contentDescription = "Camas",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacerXSmall))
                    Text(
                        text = "${beds} camas • ${baths} baños",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PropertyCardPreview() {
    ProjectTheme {
        PropertyCard(
            imageUrl = R.drawable.placeholder_property, // Asegúrate de tener esta imagen
            title = "Mansión Cerca de la UPP",
            location = "Valle de las Rosas, Puebla",
            price = "950",
            rating = 4.8f,
            beds = 3,
            baths = 2,
            isVerified = true,
            isFavorite = false,
            onCardClick = {},
            onFavoriteClick = {}
        )
    }
}