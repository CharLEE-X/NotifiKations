/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.delegate

import com.charleex.notifikations.model.PermissionState
import com.charleex.notifikations.util.CannotOpenSettingsException

internal interface PermissionDelegate {
    fun getPermissionState(): PermissionState

    suspend fun providePermission()

    @Throws(CannotOpenSettingsException::class)
    fun openSettingPage()
}
