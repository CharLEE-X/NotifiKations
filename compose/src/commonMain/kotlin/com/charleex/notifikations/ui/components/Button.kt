/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.ui.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun AppButton(
    modifier: Modifier = Modifier,
    text: String,
    bgColor: Color = MaterialTheme.colors.primary,
    textColor: Color = MaterialTheme.colors.onPrimary,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = bgColor,
            contentColor = textColor
        ),
        enabled = enabled,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            color = textColor,
        )
    }
}

@Suppress("FunctionName")
@Composable
internal fun AppButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    shape: Shape = MaterialTheme.shapes.medium,
    elevation: Dp = 12.dp,
    buttonColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onPrimary,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressed = updateTransition(label = "Card", targetState = isPressed)
    val scaleState by pressed.animateFloat(label = "Scale") { if (it) .99f else 1f }
    val elevationState by pressed.animateDp(label = "Elevation") { if (it) elevation / 2 else elevation }

    Button(
        onClick = {
            if (!isLoading) {
                onClick()
            }
        },
        enabled = isEnabled,
        shape = shape,
        elevation = ButtonDefaults.elevation(elevationState),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scaleState
                scaleY = scaleState
            },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.height(48.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = contentColor,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = label,
                    color = if (isEnabled) contentColor else MaterialTheme.colors.onSurface,
                )
            }
        }
    }
}
