package com.example.project.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    // Borde de color para el error
    val errorBorder = if (isError) Modifier.border(1.dp, ErrorRed, RoundedCornerShape(Dimens.CornerRadius)) else Modifier

    Column(modifier = modifier) {
        // --- El Contenedor que da la Sombra y Forma ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(Dimens.InputShadowElevation, RoundedCornerShape(Dimens.CornerRadius))
                .then(errorBorder) // Aplica el borde rojo si hay error
                .clip(RoundedCornerShape(Dimens.CornerRadius)),
            color = Color.White
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.InputFieldHeight),

                label = { Text(label, style = MaterialTheme.typography.bodyMedium) },

                trailingIcon = trailingIcon,
                visualTransformation = visualTransformation,
                isError = isError, // Esto es solo para la semántica, el color lo manejamos nosotros
                singleLine = true,

                // --- Colores Transparentes ---
                // Hacemos el fondo del TextField transparente para que se vea el Surface
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                    // Colores de borde (indicador)
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                    // Colores de texto y etiqueta
                    focusedLabelColor = UptelBlue,
                    unfocusedLabelColor = Color.Gray,
                    errorLabelColor = ErrorRed,
                    cursorColor = UptelBlue
                )
            )
        }

        // --- Leyenda de Error ---
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = ErrorRed,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = Dimens.SpacerSmall, top = Dimens.SpacerXSmall)
            )
        }
    }
}