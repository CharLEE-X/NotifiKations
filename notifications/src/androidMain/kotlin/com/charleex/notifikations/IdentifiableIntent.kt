/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations

import android.content.Context
import android.content.Intent

internal class IdentifiableIntent(
    private val id: String,
    packageContext: Context,
    cls: Class<*>,
) : Intent(packageContext, cls) {
    override fun filterEquals(other: Intent?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (this.id != (other as IdentifiableIntent).id) return false
        return true
    }
}
