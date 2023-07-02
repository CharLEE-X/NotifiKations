/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

internal fun openNSUrl(string: String) {
    val settingsUrl: NSURL = NSURL.URLWithString(string)!!
    if (UIApplication.sharedApplication.canOpenURL(settingsUrl)) {
        UIApplication.sharedApplication.openURL(settingsUrl)
    } else throw CannotOpenSettingsException(string)
}

internal fun openAppSettingsPage() {
    openNSUrl(UIApplicationOpenSettingsURLString)
}

internal fun CoroutineScope.observePermission(
    frequency: Long = PERMISSION_CHECK_FLOW_FREQUENCY,
    block: suspend () -> Unit,
): Job = launch {
    while (true) {
        block()
        delay(frequency)
    }
}
