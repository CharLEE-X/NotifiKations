package com.charleex.notifikations.delegate

import com.charleex.notifikations.model.PermissionState
import com.charleex.notifikations.util.CannotOpenSettingsException

internal interface PermissionDelegate {
    fun getPermissionState(): PermissionState

    suspend fun providePermission()

    @Throws(CannotOpenSettingsException::class)
    fun openSettingPage()
}
