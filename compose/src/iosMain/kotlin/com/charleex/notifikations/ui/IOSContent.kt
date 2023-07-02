/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.charleex.notifikations.ui.theme.safePaddingValues
import com.charleex.notifikations.ui.util.LocalWindow
import com.charleex.notifikations.ui.util.WindowInfo
import kotlinx.cinterop.useContents
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow

/**
 * The [MainViewController] function is responsible for displaying the content of the application.
 */
@Suppress("FunctionName", "unused")
fun MainViewController(window: UIWindow): UIViewController {
    return ComposeUIViewController {
        LaunchedEffect(window.safeAreaInsets) {
            window.safeAreaInsets.useContents {
                safePaddingValues = PaddingValues(
                    top = this.top.dp,
                    bottom = this.bottom.dp,
                    start = this.left.dp,
                    end = this.right.dp,
                )
            }
        }
        val rememberedComposeWindow by remember(window) {
            val windowInfo = window.frame.useContents {
                WindowInfo(this.size.width.dp, this.size.height.dp)
            }
            mutableStateOf(windowInfo)
        }

        CompositionLocalProvider(
            LocalWindow provides rememberedComposeWindow
        ) {
            RouterContent()
        }
    }
}
