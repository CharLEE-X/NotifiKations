/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import co.touchlab.kermit.Logger
import com.charleeex.notifikations.R
import com.charleex.notifikations.NotificationService.Companion.COLLECTING_LOCATION_NOTIFICATION_ID
import com.charleex.notifikations.delegate.NotificationLocalPermissionDelegate
import com.charleex.notifikations.delegate.readNotificationStringOption
import com.charleex.notifikations.model.NotificationSettings
import com.charleex.notifikations.model.NotificationType
import com.charleex.notifikations.model.Permission
import com.charleex.notifikations.model.PermissionState
import com.charleex.notifikations.model.notGranted
import com.charleex.notifikations.util.ONE_MINUTE_IN_MILLIS
import com.charleex.notifikations.util.PERMISSION_CHECK_FLOW_FREQUENCY
import com.charleex.notifikations.util.infiniteFlowOf
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import java.lang.System.currentTimeMillis


@Suppress("MatchingDeclarationName")
/**
 * The [NotifiKations] class implements provides methods for initializing,
 * sending notifications, and canceling notifications.
 */
actual class NotifiKations(
    private val context: Context,
    activity: Lazy<Activity>,
) : NotificationService {
    private val logger: Logger = Logger.withTag(NotifiKations::class.java.simpleName)

    private val settings = SharedPreferencesSettings.Factory(context)
        .create(NOTIFICATION_SETTINGS)

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {
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

    private val notificationLocalPermissionDelegate = NotificationLocalPermissionDelegate(
        context = context,
        activity = activity,
        notificationManagerCompat = notificationManagerCompat,
        settings = settings,
    )

    @SuppressLint("MissingPermission")
    actual override suspend fun schedule(notificationType: NotificationType): Any? {
        if (checkPermission(Permission.LOCAL_NOTIFICATIONS).notGranted()) {
            return null
        }
        return handleNotificationType(notificationType)
    }

    private fun handleNotificationType(
        notificationType: NotificationType,
        notificationId: Int = COLLECTING_LOCATION_NOTIFICATION_ID,
    ): Notification? {
        return when (notificationType) {
            is NotificationType.Scheduled -> null
            is NotificationType.Immediate.Custom -> {
                val id = try {
                    notificationType.id.toInt()
                } catch (e: NumberFormatException) {
                    logger.e {
                        "Expected notification id to be be a number, but was ${notificationType.id}."
                    }
                    null
                } ?: return null
                val notification = sendNotification(
                    id = id,
                    title = notificationType.title,
                    body = notificationType.body,
                )
                logger.i { "Sending notification: $notification." }
                notificationManager.notify(notificationId, notification)
                notification
            }
        }
    }

    actual override suspend fun cancelNotifications(ids: List<String>) {
        if (ids.isEmpty()) return
        ids.forEach { notificationId ->
            try {
                logger.v { "Cancelling pending notification with ID: $notificationId" }
                alarmManager.cancel(createPendingIntent(notificationId.length))
                notificationManager.cancel(notificationId.toInt())
            } catch (e: NumberFormatException) {
                logger.w(e) { "Couldn't find notification with ID '$notificationId'." }
            }
        }
    }

    actual override fun checkPermission(permission: Permission): PermissionState {
        return notificationLocalPermissionDelegate.getPermissionState()
    }

    actual override fun permissionState(permission: Permission): Flow<PermissionState> {
        return infiniteFlowOf(PERMISSION_CHECK_FLOW_FREQUENCY) { checkPermission(permission) }
    }

    actual override suspend fun providePermission(permission: Permission) {
        notificationLocalPermissionDelegate.providePermission()
    }

    actual override fun openSettingPage(permission: Permission) {
        try {
            notificationLocalPermissionDelegate.openSettingPage()
        } catch (e: ActivityNotFoundException) {
            logger.e(e) { "Couldn't open setting page." }
        }
    }

    @SuppressLint("MissingPermission") // Permission checked in "schedule" function.
    private fun sendNotification(
        id: Int,
        title: String,
        body: String?,
    ): Notification {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            // This icon will need to come from the config.
            setSmallIcon(R.drawable.ic_baseline_directions_car_24)
            setContentTitle(title)
            setContentText(body)
            priority = NotificationManagerCompat.IMPORTANCE_HIGH
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                setCategory(Notification.CATEGORY_NAVIGATION) // shows the notification immediately
            } else {
                setCategory(Notification.CATEGORY_SERVICE)
            }
            setOngoing(true)
            setOnlyAlertOnce(true)
            setWhen(currentTimeMillis() + ONE_MINUTE_IN_MILLIS)
        }

        logger.v {
            "Scheduling local notification $id, time ${
                Instant.fromEpochMilliseconds(
                    currentTimeMillis() + ONE_MINUTE_IN_MILLIS
                )
            }."
        }
        return builder.build()
    }

    private fun createPendingIntent(
        id: Int,
        intentTransform: Intent.() -> Unit = {},
    ): PendingIntent {
        val intent = IdentifiableIntent("$id", context, NotificationPublisher::class.java).apply(
            intentTransform
        )
        return PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
        internal const val NOTIFICATION_TYPE_DISMISS = "DISMISS"
        internal const val NOTIFICATION_PAYLOAD_ID = "NOTIFICATION_PAYLOAD_ID"
        internal const val NOTIFICATION_PAYLOAD_TYPE = "NOTIFICATION_PAYLOAD_TYPE"
        internal const val NOTIFICATION_PAYLOAD_NOTIFICATION = "NOTIFICATION_PAYLOAD_NOTIFICATION"
        internal const val NOTIFICATION_SETTINGS = "NOTIFIKATION_SETTINGS"
    }
}
