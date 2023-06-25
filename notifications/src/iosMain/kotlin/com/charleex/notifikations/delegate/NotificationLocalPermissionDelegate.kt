/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.delegate

import com.charleex.notifikations.model.PermissionState
import com.charleex.notifikations.util.observePermission
import com.charleex.notifikations.util.openAppSettingsPage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusEphemeral
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNNotificationSettingEnabled
import platform.UserNotifications.UNUserNotificationCenter

internal class NotificationLocalPermissionDelegate(
    private val notificationCenter: UNUserNotificationCenter,
    coroutineScope: CoroutineScope,
) : PermissionDelegate {
    private var checkNotificationJob: Job? = null
    private var _permissionState: PermissionState = PermissionState.NOT_DETERMINED

    init {
        // Reason for getting state of permission this way is that "getNotificationSettingsWithCompletionHandler"
        // is async, and we must to wait for it to finish before we can return the state of permission.
        // This permission is required immediately after this class is initiated.
        // Using CompletableDeferred or async makes AppStateObserver crash the app.
        // Reason: "Job has not completed yet."
        checkNotificationJob = coroutineScope.observePermission {
            checkPermission()
        }
    }

    override fun getPermissionState(): PermissionState {
        return _permissionState
    }

    override suspend fun providePermission() {
        notificationCenter.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
        ) { _, _ ->
            checkPermission()
        }
    }

    override fun openSettingPage() {
        openAppSettingsPage()
    }

    private fun checkPermission() {
        notificationCenter.getNotificationSettingsWithCompletionHandler { settings ->
            settings ?: return@getNotificationSettingsWithCompletionHandler
            _permissionState = when {
                settings.alertSetting != UNNotificationSettingEnabled ||
                        settings.badgeSetting != UNNotificationSettingEnabled ||
                        settings.lockScreenSetting != UNNotificationSettingEnabled ||
                        settings.notificationCenterSetting() != UNNotificationSettingEnabled ||
                        settings.authorizationStatus == UNAuthorizationStatusDenied ||
                        settings.authorizationStatus == UNAuthorizationStatusEphemeral
                -> PermissionState.DENIED

                settings.authorizationStatus == UNAuthorizationStatusAuthorized ||
                        settings.authorizationStatus == UNAuthorizationStatusProvisional
                -> PermissionState.GRANTED

                settings.authorizationStatus == UNAuthorizationStatusNotDetermined
                -> PermissionState.NOT_DETERMINED

                else -> PermissionState.NOT_DETERMINED
            }
        }
    }
}
