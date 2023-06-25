/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.ui.util

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp

internal data class WindowInfo(val width: Dp, val height: Dp, val minDimen: Dp? = null, val maxDimen: Dp? = null)

internal val LocalWindow = compositionLocalOf { WindowInfo(Dp.Unspecified, Dp.Unspecified) }
