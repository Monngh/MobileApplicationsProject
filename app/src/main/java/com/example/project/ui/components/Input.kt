package com.example.project.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.ErrorRed
import com.example.project.ui.theme.UptelBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,  // ← NUEVO: Ícono al inicio
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    isSuccess: Boolean = false,  // ← NUEVO: Estado de éxito
    helperText: String? = null,  // ← NUEVO: Texto de ayuda
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    placeholder: String? = null  // ← NUEVO: Placeholder
) {
    // Estado de interacción para animaciones
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Animaciones suaves
    val elevation by animateDpAsState(
        targetValue = if (isFocused) 8.dp else 4.dp,
        animationSpec = tween(300, easing = EaseInOut),
        label = "elevation"
    )

    val borderWidth by animateDpAsState(
        targetValue = if (isFocused) 2.dp else 0.dp,
        animationSpec = tween(300),
        label = "border"
    )

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    // Colores dinámicos
    val borderColor = when {
        isError -> ErrorRed
        isSuccess -> Color(0xFF4CAF50)
        isFocused -> UptelBlue
        else -> Color.Transparent
    }

    Column(modifier = modifier) {
        // --- TextField con Surface ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .shadow(
                    elevation = elevation,
                    shape = RoundedCornerShape(Dimens.CornerRadius),
                    spotColor = if (isFocused) UptelBlue.copy(alpha = 0.25f) else Color.Black.copy(alpha = 0.1f)
                ),
            shape = RoundedCornerShape(Dimens.CornerRadius),
            color = if (enabled) Color.White else Color.White.copy(alpha = 0.6f),
            border = if (borderWidth > 0.dp) {
                androidx.compose.foundation.BorderStroke(borderWidth, borderColor)
            } else null
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                placeholder = placeholder?.let {
                    { Text(it, style = MaterialTheme.typography.bodySmall) }
                },
                leadingIcon = leadingIcon?.let {
                    {
                        Box(
                            modifier = Modifier.padding(start = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            it()
                        }
                    }
                },
                trailingIcon = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        // Indicador de estado
                        AnimatedVisibility(
                            visible = isSuccess && !isError,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Válido",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        AnimatedVisibility(
                            visible = isError,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = "Error",
                                tint = ErrorRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Ícono personalizado del usuario
                        trailingIcon?.invoke()
                    }
                },
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                isError = isError,
                singleLine = singleLine,
                maxLines = maxLines,
                enabled = enabled,
                readOnly = readOnly,
                interactionSource = interactionSource,
                colors = TextFieldDefaults.colors(
                    // Fondos transparentes
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                    // Indicadores (líneas inferiores) - todos transparentes
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                    // Colores de texto
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Gray,
                    errorTextColor = Color.Black,

                    // Colores de etiqueta
                    focusedLabelColor = UptelBlue,
                    unfocusedLabelColor = Color.Gray,
                    errorLabelColor = ErrorRed,
                    disabledLabelColor = Color.Gray.copy(alpha = 0.5f),

                    // Cursor
                    cursorColor = UptelBlue,
                    errorCursorColor = ErrorRed
                )
            )
        }

        // --- Mensajes de ayuda y error con animación ---
        AnimatedVisibility(
            visible = (isError && errorMessage != null) || (!isError && helperText != null),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp, end = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val message = if (isError && errorMessage != null) {
                    errorMessage
                } else {
                    helperText ?: ""
                }

                val color = if (isError) ErrorRed else Color.Gray

                Text(
                    text = message,
                    color = color,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = if (leadingIcon != null) 0.dp else 0.dp)
                )
            }
        }
    }
}

// ========================================
// VARIANTE: Input con contador de caracteres
// ========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputWithCounter(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLength: Int,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val isOverLimit = value.length > maxLength

    Column(modifier = modifier) {
        Input(
            value = value,
            onValueChange = { if (it.length <= maxLength) onValueChange(it) },
            label = label,
            leadingIcon = leadingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            isError = isError || isOverLimit,
            errorMessage = errorMessage
        )

        // Contador
        Text(
            text = "${value.length} / $maxLength",
            style = MaterialTheme.typography.labelSmall,
            color = if (isOverLimit) ErrorRed else Color.Gray,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 8.dp, top = 2.dp)
        )
    }
}
