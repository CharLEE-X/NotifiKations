/** * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

internal object Dimensions {
    const val TOP_BAR_HEIGHT = 56
    const val BOTTOM_BAR_HEIGHT = 56
}

internal object Padding {
    val quarter = 4.dp
    val half = 8.dp
    val default = 16.dp
    val double = 32.dp
}

// This value is set only in iOS
internal var safePaddingValues by mutableStateOf(PaddingValues())
