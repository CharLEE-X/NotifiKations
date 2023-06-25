package com.charleex.notifikations.delegate

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.charleex.notifications.R
import com.charleex.notifikations.NotifiKations
import com.charleex.notifikations.model.NotificationSettings
import com.charleex.notifikations.model.Permission
import com.charleex.notifikations.model.PermissionState
import com.russhwolf.settings.Settings
import com.txconnected.mobox.component.permissions.util.checkPermissions
import com.txconnected.mobox.component.permissions.util.openAppSettingsPage
import kotlinx.serialization.json.Json

internal class NotificationLocalPermissionDelegate(
    private val context: Context,
    private val activity: Lazy<Activity>,
    private val notificationManagerCompat: NotificationManagerCompat,
    private val settings: Settings,
) : PermissionDelegate {
    override fun getPermissionState(): PermissionState {
        return checkPermissions(context, activity, localNotificationPermissions)
    }

    override suspend fun providePermission() {
        val notificationStrings = settings.readNotificationStringOption()
        val name = notificationStrings?.get(NotificationSettings.NOTIFICATION_CHANNEL_TITLE.name)
            ?: context.getString(R.string.notification_channel_title)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description =
                notificationStrings?.get(NotificationSettings.NOTIFICATION_CHANNEL_DESCRIPTION.name)
                    ?: context.getString(R.string.notification_channel_description)
        }
        notificationManagerCompat.createNotificationChannel(channel)
    }

    override fun openSettingPage() {
        context.openAppSettingsPage(Permission.LOCAL_NOTIFICATIONS)
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "1"
    }
}

private val localNotificationPermissions: List<String> =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.POST_NOTIFICATIONS)
    } else emptyList()

fun Settings.readNotificationStringOption(): Map<String, String>? {
    val notificationStringMap = this.getStringOrNull(NotifiKations.NOTIFIKATION_SETTINGS)
    return notificationStringMap?.let { Json.decodeFromString(it) }
}