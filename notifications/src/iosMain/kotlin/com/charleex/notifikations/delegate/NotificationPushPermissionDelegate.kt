/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.delegate

import com.charleex.notifikations.model.PermissionState
import com.charleex.notifikations.util.observePermission
import com.charleex.notifikations.util.openAppSettingsPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import platform.UIKit.UIApplication
import platform.UIKit.isRegisteredForRemoteNotifications
import platform.UIKit.registerForRemoteNotifications
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter

internal class NotificationPushPermissionDelegate(
    private val notificationCenter: UNUserNotificationCenter,
    coroutineScope: CoroutineScope,
) : PermissionDelegate {
    private var checkNotificationJob: Job? = null
    private var _permissionState: PermissionState = PermissionState.NOT_DETERMINED

    init {
        // iOS complaining when checking `isRegisteredForRemoteNotifications()` on the main thread.
        checkNotificationJob = coroutineScope.observePermission {
            checkPermission()
        }
    }


    override fun getPermissionState(): PermissionState {
        return _permissionState
    }

    override suspend fun providePermission() {
        UIApplication.sharedApplication.registerForRemoteNotifications()
        requestNotificationAuthorization()
    }

    override fun openSettingPage() {
        openAppSettingsPage()
    }

    private suspend fun checkPermission() {
        // Checking permission state on the background thread gives error in logs.
        val registered = withContext(Dispatchers.Main) {
            UIApplication.sharedApplication.isRegisteredForRemoteNotifications()
        }
        val permissionState = if (registered)
            PermissionState.GRANTED else PermissionState.NOT_DETERMINED
        _permissionState = permissionState
    }

    private fun requestNotificationAuthorization() {
        notificationCenter.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
        ) { _, _ -> }
    }
}
