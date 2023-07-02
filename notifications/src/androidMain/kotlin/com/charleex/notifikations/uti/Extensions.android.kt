/**
 * Copyright (c) 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.uti

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.charleex.notifikations.model.PermissionState

internal fun Context.openPage(
    action: String,
    newData: Uri? = null,
) {
    val intent = Intent(action).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        newData?.let { data = it }
    }
    startActivity(intent)
}

internal fun checkPermissions(
    context: Context,
    activity: Lazy<Activity>,
    permissions: List<String>,
): PermissionState {
    permissions.ifEmpty { return PermissionState.GRANTED } // no permissions needed
    val status: List<Int> = permissions.map {
        context.checkSelfPermission(it)
    }
    val isAllGranted: Boolean = status.all { it == PackageManager.PERMISSION_GRANTED }
    if (isAllGranted) return PermissionState.GRANTED

    // SDK starts checking permissions before activity is available, so until we have activity
    // we can't check permission rationale, so we return NOT_DETERMINED. It makes not difference
    // for AppState, because permission is not granted anyway. Code below matters only in the UI,
    // but the Activity is ready at this point.
    val isAllRequestRationale: Boolean = permissions.all {
        !activity.value.shouldShowRequestPermissionRationale(it)
    }
    return if (isAllRequestRationale) PermissionState.NOT_DETERMINED
    else PermissionState.DENIED
}

private const val REQUEST_CODE = 100

internal fun Activity.providePermissions(
    permissions: List<String>,
    onError: (Throwable) -> Unit,
) {
    try {
        ActivityCompat.requestPermissions(
            this, permissions.toTypedArray(), REQUEST_CODE
        )
    } catch (e: ActivityNotFoundException) {
        onError(e)
    }
}

internal fun Context.openAppSettingsPage() {
    openPage(
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        newData = Uri.parse("package:$packageName"),
    )
}
