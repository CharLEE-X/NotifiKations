/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.model

import kotlinx.serialization.Serializable

/**
 * This enum represents the permissions used in the application.
 * It provides constant values for various permissions related to system services and features.
 */
@Serializable
enum class Permission {
    /**
     * App push notifications permission. Can be requested manually on iOS only.
     */
    PUSH_NOTIFICATIONS,

    /**
     * App local notifications permission.
     */
    LOCAL_NOTIFICATIONS
}
