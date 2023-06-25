/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> infiniteFlowOf(
    delay: Long = 16, // 16ms is 1 frame in a 60Hz/fps display, so no noticeable difference
    item: suspend () -> T,
): Flow<T> = flow {
    while (true) {
        emit(item())
        delay(delay)
    }
}
