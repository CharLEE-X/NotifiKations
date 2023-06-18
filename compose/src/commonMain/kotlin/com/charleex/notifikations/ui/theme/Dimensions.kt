package com.charleex.notifikations.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

internal object Dimensions {
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
