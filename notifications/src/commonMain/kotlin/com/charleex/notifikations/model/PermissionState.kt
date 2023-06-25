/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.model

import kotlinx.serialization.Serializable

/**
 * Represents the state of a permission
 */
@Serializable
enum class PermissionState {
    /**
     * Indicates that the permission has not been requested yet
     */
    NOT_DETERMINED,

    /**
     * Indicates that the permission has been requested and accepted.
     */
    GRANTED,

    /**
     * Indicates that the permission has been requested but the user denied the permission
     */
    DENIED;
}

/**
 * Extension function to check if the permission is not granted
 */
fun PermissionState.notGranted(): Boolean {
    return this != PermissionState.GRANTED
}
