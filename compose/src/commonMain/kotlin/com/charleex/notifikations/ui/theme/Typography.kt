/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
internal fun typography() = Typography(
    h1 = TextStyle(
        color = MaterialTheme.colors.onBackground,
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp
    ),
)
