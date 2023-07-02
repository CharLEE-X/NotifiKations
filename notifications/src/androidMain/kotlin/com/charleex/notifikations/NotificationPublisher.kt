/**
 * Copyright (c) 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class NotificationPublisher : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId =
            intent.getIntExtra(NotifiKations.NOTIFICATION_PAYLOAD_ID, 0)
        val notificationType =
            intent.getStringExtra(NotifiKations.NOTIFICATION_PAYLOAD_TYPE)

        @Suppress("DEPRECATION")
        val notification =
            intent.getParcelableExtra<Notification>(NotifiKations.NOTIFICATION_PAYLOAD_NOTIFICATION)

        if (notificationType == NotifiKations.NOTIFICATION_TYPE_DISMISS) {
            notificationManager.cancel(notificationId)
        } else {
            notificationManager.notify(notificationId, notification)
        }
    }
}
