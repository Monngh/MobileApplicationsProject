package com.example.project.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.project.ui.theme.Dimens
import com.example.project.ui.theme.UptelBlue

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Animación de la sombrita
    val elevation by animateDpAsState(
        // Sombra más grande al presionar
        targetValue = if (isPressed) Dimens.ButtonElevation * 2 else Dimens.ButtonElevation,
        label = "elevationAnimation"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ButtonHeight),
        enabled = enabled,
        // --- ¡ACTUALIZADO! ---
        shape = RoundedCornerShape(Dimens.CornerRadius),
        interactionSource = interactionSource,

        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = elevation,
            disabledElevation = 0.dp
        ),

        colors = ButtonDefaults.buttonColors(
            containerColor = UptelBlue,
            contentColor = Color.White,
            disabledContainerColor = Color.Gray
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}