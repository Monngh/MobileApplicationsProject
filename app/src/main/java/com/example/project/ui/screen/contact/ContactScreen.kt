package com.example.project.ui.screen.contact

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.project.R
import com.example.project.ui.components.InfoPageTemplate
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.TextSecondary

@Composable
fun ContactScreen(onOpenDrawer: () -> Unit) {
    InfoPageTemplate(
        title = "Contacto",
        onOpenDrawer = onOpenDrawer
    ) {
        // Tarjeta principal de contacto
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(24.dp),
                    clip = true
                ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 32.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icono decorativo
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                )
                            ),
                            RoundedCornerShape(20.dp)
                        )
                        .clip(RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.ContactSupport,
                        contentDescription = "Contacto",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Título
                Text(
                    text = "¿Necesitas ayuda?",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Descripción
                Text(
                    text = "Estamos aquí para resolver tus dudas y escuchar tus sugerencias. " +
                            "Contáctanos a través de los siguientes canales:",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Tarjeta de información de contacto
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.05f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Email
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    CircleShape
                                )
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Email",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Correo Electrónico",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "soporte.domy@dominio.com",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Respuesta en 24-48 horas",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            )
                        }
                    }

                    // Divider
                    Divider(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )

                    // Teléfono/WhatsApp
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    CircleShape
                                )
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = "Teléfono",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Teléfono / WhatsApp",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "+52 (123) 456-7890",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Disponible de 9:00 AM a 6:00 PM",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            )
                        }
                    }

                    // Divider
                    Divider(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )

                    // Dirección
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    CircleShape
                                )
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Ubicación",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Oficina Central",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Av. Universidad 123, Ciudad Universitaria",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "CDMX, 04510",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                /*
               // Botones de acción
               Column(
                   modifier = Modifier.fillMaxWidth(),
                   verticalArrangement = Arrangement.spacedBy(12.dp)
               ) {
                   // Botón para enviar email
                   Button(
                       onClick = { /* TODO: Abrir cliente de email */ },
                       modifier = Modifier
                           .fillMaxWidth()
                           .height(56.dp),
                       shape = RoundedCornerShape(16.dp),
                       colors = ButtonDefaults.buttonColors(
                           containerColor = MaterialTheme.colorScheme.primary,
                           contentColor = Color.White
                       ),
                       elevation = ButtonDefaults.buttonElevation(
                           defaultElevation = 4.dp,
                           pressedElevation = 8.dp
                       )
                   ) {
                       Icon(
                           Icons.Default.Email,
                           contentDescription = "Enviar email",
                           modifier = Modifier.size(20.dp)
                       )
                       Spacer(modifier = Modifier.width(12.dp))
                       Text(
                           text = "Enviar un correo",
                           style = MaterialTheme.typography.titleSmall.copy(
                               fontWeight = FontWeight.SemiBold
                           )
                       )
                   }

                   // Botón para llamar
                   OutlinedButton(
                       onClick = { /* TODO: Abrir marcador telefónico */ },
                       modifier = Modifier
                           .fillMaxWidth()
                           .height(56.dp),
                       shape = RoundedCornerShape(16.dp),
                       colors = ButtonDefaults.outlinedButtonColors(
                           containerColor = Color.Transparent,
                           contentColor = MaterialTheme.colorScheme.primary
                       ),
                       border = BorderStroke(
                           width = 1.5.dp,
                           color = MaterialTheme.colorScheme.primary
                       )
                   ) {
                       Icon(
                           Icons.Default.Phone,
                           contentDescription = "Llamar",
                           modifier = Modifier.size(20.dp)
                       )
                       Spacer(modifier = Modifier.width(12.dp))
                       Text(
                           text = "Llamar ahora",
                           style = MaterialTheme.typography.titleSmall.copy(
                               fontWeight = FontWeight.SemiBold
                           )
                       )
                   }
               }
*/

                // Horarios de atención
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "📅 Horarios de atención",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Lunes a Viernes: 9:00 AM - 6:00 PM\n" +
                                    "Sábados: 10:00 AM - 2:00 PM\n" +
                                    "Domingos: Cerrado",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}