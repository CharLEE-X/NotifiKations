/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations

import co.touchlab.kermit.Logger
import com.benasher44.uuid.uuid4
import com.charleex.notifikations.delegate.NotificationLocalPermissionDelegate
import com.charleex.notifikations.model.NotificationType
import com.charleex.notifikations.model.Permission
import com.charleex.notifikations.model.PermissionState
import com.charleex.notifikations.model.SoundType
import com.charleex.notifikations.model.notGranted
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSCalendarUnitTimeZone
import platform.Foundation.NSCalendarUnitYear
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject
import kotlin.time.Duration.Companion.seconds

@Suppress("MatchingDeclarationName")
/**
 * The [NotifiKations] class implements provides methods for initializing,
 * sending notifications, and canceling notifications.
 */
actual class NotifiKations(coroutineScope: CoroutineScope) : NotificationService {
    private val logger = Logger.withTag(NotifiKations::class.simpleName!!)

    private val _delegate = object : UNUserNotificationCenterDelegateProtocol, NSObject() {
        override fun userNotificationCenter(
            center: UNUserNotificationCenter,
            willPresentNotification: UNNotification,
            withCompletionHandler: (UNNotificationPresentationOptions) -> Unit,
        ) {
            withCompletionHandler(UNNotificationPresentationOptionAlert)
        }
    }

    private val notificationCenter: UNUserNotificationCenter by lazy {
        UNUserNotificationCenter.currentNotificationCenter().apply {
            delegate = _delegate
        }
    }

    private val notificationLocalPermissionDelegate = NotificationLocalPermissionDelegate(
        notificationCenter = notificationCenter,
        coroutineScope = coroutineScope,
    )

    actual override suspend fun schedule(notificationType: NotificationType): Any? {
        if (checkPermission(Permission.LOCAL_NOTIFICATIONS).notGranted()
        ) return null
        return when (notificationType) {
            is NotificationType.Immediate.Custom -> {
                sendNotification(
                    id = uuid4().toString(),
                    title = notificationType.title,
                    body = notificationType.body,
                    delivery = Clock.System.now() + 1.seconds,
                    soundType = notificationType.soundType
                )
            }

            is NotificationType.Scheduled -> {
                sendNotification(
                    id = notificationType.id,
                    title = notificationType.title,
                    body = notificationType.body,
                    delivery = notificationType.delivery,
                    soundType = notificationType.soundType
                )
            }
        }
    }

    actual override suspend fun cancelNotifications(ids: List<String>) {
        ids.ifEmpty { return }
        logger.v { "Cancelling pending notifications with IDs: [${ids.joinToString()}]" }
        notificationCenter.removePendingNotificationRequestsWithIdentifiers(ids)
        notificationCenter.removeDeliveredNotificationsWithIdentifiers(ids)
    }

    actual override fun checkPermission(permission: Permission): PermissionState {
        return notificationLocalPermissionDelegate.getPermissionState()
    }

    actual override suspend fun providePermission(permission: Permission) {
        notificationLocalPermissionDelegate.providePermission()
    }

    actual override fun openSettingPage(permission: Permission) {
        notificationLocalPermissionDelegate.openSettingPage()
    }

    private suspend fun sendNotification(
        id: String,
        title: String,
        body: String?,
        delivery: Instant,
        soundType: SoundType,
    ): String? {
        logger.v { "Scheduling local notification at ${delivery.toNSDate().description}." }
        val deliveryDate = delivery.toNSDate()
        val allUnits =
            NSCalendarUnitSecond or
                    NSCalendarUnitMinute or
                    NSCalendarUnitHour or
                    NSCalendarUnitDay or
                    NSCalendarUnitMonth or
                    NSCalendarUnitYear or
                    NSCalendarUnitTimeZone
        val dateComponents = NSCalendar.currentCalendar.components(allUnits, deliveryDate)
        // Using UNTimeIntervalNotificationTrigger is unstable in KMM, so we use UNCalendarNotificationTrigger instead.
        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            dateComponents = dateComponents,
            repeats = false
        )
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            body?.let { setBody(it) }
            soundType.toUNNotificationSound()?.let { setSound(it) }
        }
        val request = UNNotificationRequest.requestWithIdentifier(
            id, content, trigger
        )
        val completable = CompletableDeferred<String?>()
        notificationCenter.addNotificationRequest(request) {
            completable.complete(it?.localizedDescription)
        }
        val error = completable.await()
        return if (error == null) {
            logger.v { "Notification scheduled with ID: $id" }
            id
        } else {
            logger.i { "Failed to schedule notification with ID: $id, error: $error" }
            null
        }
    }
}

private fun SoundType.toUNNotificationSound(): UNNotificationSound? {
    return when (this) {
        SoundType.NONE -> null
        SoundType.DEFAULT -> UNNotificationSound.defaultSound()
        SoundType.CRITICAL -> UNNotificationSound.defaultCriticalSound()
    }
}
