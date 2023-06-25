/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.ui.theme

import androidx.compose.ui.graphics.Color

internal object AppColors {
    val grey = Color(0xFF868686)
    val darkGrey51 = Color(0xFF333333)
    val darkGrey32 = Color(0xFF202020)
    val lightGrey220 = Color(0xFFdcdcdc)
    val lightGrey222 = Color(0xFFDEDEDE)
    val lightGrey250 = Color(0xFFfafafa)

    val moboxGreyLighter = Color(0xFFeaeeee)
    val moboxGreyLight = Color(0xFFEAECED)
    val moboxGreyMedium = Color(0xFF7289A4)
    val moboxGreyDark = Color(0xFF2f455e)
    val primary = Color(0xFF1AA38F)
    val primaryVariant = Color(0xFF156351)
    val onPrimary = moboxGreyDark

    val secondary = Color(0xFFfdd455)
    val onSecondary = moboxGreyDark

    val background = Color(0xFFFFFFFF)
    val onBackground = Color(0xFF333333)

    val surface = Color(0xFFFFFFFF)
    val onSurface = Color(0xFF333333)

    val error = Color(0xFFf1582c)

    object Rating {
        val bad = Color(0xFFFF6B00)
        val average = Color(0xFFFFBC39)
        val good = Color(0xFF00B2FF)
    }
}
