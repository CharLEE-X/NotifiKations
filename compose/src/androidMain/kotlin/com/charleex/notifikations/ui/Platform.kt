package com.charleex.notifikations.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.charleex.notifikations.NotifiKations

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

@Composable
internal actual fun provideNotifiKations(): NotifiKations {
    val context = LocalContext.current
    val activity = context.findActivity()

    return NotifiKations(
        context = context,
        activity = lazyOf(activity),
    )
}
