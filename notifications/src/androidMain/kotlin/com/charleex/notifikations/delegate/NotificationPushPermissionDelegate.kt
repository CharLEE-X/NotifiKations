/**
 * Copyright (c) 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.delegate

import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.charleex.notifikations.model.PermissionState
import com.charleex.notifikations.uti.openAppSettingsPage

internal class NotificationPushPermissionDelegate(
    private val context: Context,
    private val notificationManagerCompat: NotificationManagerCompat,
) : PermissionDelegate {
    override fun getPermissionState(): PermissionState {
        val isGranted = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            notificationManagerCompat.areNotificationsEnabled() else true
        return if (isGranted)
            PermissionState.GRANTED else PermissionState.DENIED
    }

    override suspend fun providePermission() {

        openSettingPage()
    }

    override fun openSettingPage() {
        context.openAppSettingsPage()
    }
}
