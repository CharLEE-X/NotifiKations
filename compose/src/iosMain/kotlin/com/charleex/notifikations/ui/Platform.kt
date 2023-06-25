package com.charleex.notifikations.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.charleex.notifikations.NotifiKations


@Composable
internal actual fun provideNotifiKations(): NotifiKations {
    val coroutineScope = rememberCoroutineScope()
    return NotifiKations(
        coroutineScope = coroutineScope,
    )
}
