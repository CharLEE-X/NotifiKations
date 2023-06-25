package com.charleex.notifikations.delegate

import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.charleex.notifikations.model.Permission
import com.charleex.notifikations.model.PermissionState
import com.txconnected.mobox.component.permissions.util.openAppSettingsPage

internal class NotificationPushPermissionDelegate(
    private val context: Context,
    private val notificationManagerCompat: NotificationManagerCompat,
) : PermissionDelegate {
    override fun getPermissionState(): PermissionState {
        val granted = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            notificationManagerCompat.areNotificationsEnabled() else true
        return if (granted)
            PermissionState.GRANTED else PermissionState.DENIED
    }

    override suspend fun providePermission() {

        openSettingPage()
    }

    override fun openSettingPage() {
        context.openAppSettingsPage(Permission.PUSH_NOTIFICATIONS)
    }
}
