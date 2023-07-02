/**
 * Copyright (c) 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations

import com.charleex.notifikations.model.NotificationType
import com.charleex.notifikations.model.Permission
import com.charleex.notifikations.model.PermissionState
import kotlinx.coroutines.flow.Flow

@Suppress("MatchingDeclarationName", "UndocumentedPublicClass")
/**
 * The NotificationSchedulingService interface provides methods for initializing,
 * sending notifications, and canceling notifications.
 */
expect class NotifiKations : NotificationService {
    override suspend fun schedule(notificationType: NotificationType): Any?
    override suspend fun cancelNotifications(ids: List<String>)
    override fun checkPermission(permission: Permission): PermissionState
    override fun permissionState(permission: Permission): Flow<PermissionState>
    override suspend fun providePermission(permission: Permission)
    override fun openSettingPage(permission: Permission)
}
