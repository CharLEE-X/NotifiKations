/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.util

internal class CannotOpenSettingsException(permissionName: String) :
    Exception("Cannot open settings for permission $permissionName.")

internal class PermissionRequestException(permissionName: String) :
    Exception("Failed to request $permissionName permission.")
