/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = AppColors.primary,
    primaryVariant = AppColors.primary,
    onPrimary = Color.White,
    secondary = AppColors.secondary,
    secondaryVariant = AppColors.secondary,
    onSecondary = Color.White,
    background = Color(0xFFf1f3f4),
    onBackground = AppColors.darkGrey51,
    surface = Color.White,
    onSurface = AppColors.moboxGreyDark,
    error = AppColors.error,
)

private val DarkColorPalette = darkColors(
    primary = AppColors.primary,
    primaryVariant = AppColors.primary,
    onPrimary = AppColors.onPrimary,
    secondary = AppColors.secondary,
    secondaryVariant = AppColors.secondary,
    onSecondary = AppColors.onSecondary,
    background = AppColors.darkGrey32,
    onBackground = AppColors.lightGrey220,
    surface = AppColors.darkGrey51,
    onSurface = AppColors.moboxGreyLighter,
    error = AppColors.error,
)

@Suppress("FunctionName")
@Composable
internal fun ApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography(),
        shapes = shapes,
    ) {
        Surface(
            modifier = Modifier.background(MaterialTheme.colors.background)
        ) {
            content()
        }
    }
}
