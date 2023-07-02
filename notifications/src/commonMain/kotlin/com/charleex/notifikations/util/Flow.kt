/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Creates an infinite [Flow] that emits items at a specified delay.
 *
 * @param T value type
 * @param delay The delay between emitting items, in milliseconds.
 * @param item The suspending function that provides the item to emit.
 * @return The infinite Flow that emits items with the specified delay.
 */
fun <T> infiniteFlowOf(
    delay: Long = 16, // 16ms is 1 frame in a 60Hz/fps display, so no noticeable difference
    item: suspend () -> T,
): Flow<T> = flow {
    while (true) {
        emit(item())
        delay(delay)
    }
}
