package com.charleex.notifikations

import com.charleex.notifikations.model.NotificationType
import com.charleex.notifikations.model.Permission
import com.charleex.notifikations.model.PermissionState
import com.charleex.notifikations.util.PERMISSION_CHECK_FLOW_FREQUENCY
import com.charleex.notifikations.util.infiniteFlowOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * The NotificationSchedulingService interface provides methods for initializing,
 * sending notifications, and canceling notifications.
 */
expect class NotifiKations : NotificationService

interface NotificationService {
    /**
     * Sends a notification of the specified type.
     * @param notificationType The type of notification to send.
     * @return The notification message sent, or null if the notification failed.
     */
    suspend fun schedule(notificationType: NotificationType): Any?

    /**
     * Cancels notifications with the specified IDs.
     * @param ids The list of notification IDs to cancel.
     */
    suspend fun cancelNotifications(ids: List<String>)

    /**
     * Checks the current state of notification permission.
     *
     * @param permission The permission to check.
     * @return The current state of the permission.
     */
    fun checkPermission(permission: Permission): PermissionState

    /**
     * Returns a Flow that emits the PermissionState whenever it changes for the specified permission.
     * The emission frequency is determined by the PERMISSION_CHECK_FLOW_FREQUENCY constant.
     *
     * @param permission The permission to monitor.
     * @return A flow of permission states for the specified permission.
     */
    fun permissionState(permission: Permission): Flow<PermissionState> {
        return infiniteFlowOf(PERMISSION_CHECK_FLOW_FREQUENCY) { checkPermission(permission) }
    }

    /**
     * Requests the application to provide the specified permission.
     *
     * @param permission The permission to be provided.
     */
    suspend fun providePermission(permission: Permission)

    /**
     * Opens the settings page for the specified permission within the application or the device's settings app.
     *
     * @param permission The permission for which to open the settings page.
     */
    fun openSettingPage(permission: Permission)

    companion object {
        /**
         * The ID for the collecting location notification.
         */
        const val COLLECTING_LOCATION_NOTIFICATION_ID = 1
    }
}

