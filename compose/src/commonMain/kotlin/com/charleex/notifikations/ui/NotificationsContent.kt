/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.charleex.notifikations.NotifiKations
import com.charleex.notifikations.NotificationService
import com.charleex.notifikations.model.NotificationType
import com.charleex.notifikations.model.SoundType
import com.charleex.notifikations.ui.components.AppButton
import com.charleex.notifikations.ui.components.AppTextField
import com.charleex.notifikations.ui.components.ExpandableCard
import com.charleex.notifikations.ui.theme.Padding
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun NotificationsContent(notifiKations: NotifiKations) {
    Column(
        modifier = Modifier
            .padding(Padding.default)
            .fillMaxWidth(),
    ) {
        SettingsSection(title = "Notifications") {
            LocationNotificationContent(notifiKations)
        }
    }
}

@Composable
internal fun SettingsSection(
    title: String,
    extraTitle: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Padding.half),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row {
            Text(
                text = title,
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onSurface
            )
            Box(modifier = Modifier.weight(1f))
            extraTitle()
        }
        content()
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun LocationNotificationContent(notifiKations: NotifiKations) {
    val scope = rememberCoroutineScope()

    var hasLocationNotification by remember { mutableStateOf(false) }
    var instantNotificationTitle by remember {
        mutableStateOf("Lose Yourself in Your Journey")
    }
    var instantNotificationDescription by remember {
        mutableStateOf(
            "Assessing if you're making moves, seizing opportunities like Eminem. " +
                    "Stay tuned for the ultimate success story!"
        )
    }

    var scheduledNotificationTitle by remember {
        mutableStateOf("Lose Yourself in Your Journey")
    }
    var scheduledNotificationDescription by remember {
        mutableStateOf(
            "Assessing if you're making moves, seizing opportunities like Eminem. " +
                    "Stay tuned for the ultimate success story!"
        )
    }

    @Suppress("MagicNumber")
    var scheduledNotificationDelay: Int by remember { mutableStateOf(5) }

    ExpandableCard(headerText = "Collecting location notification", expanded = true) {
        Button(
            onClick = {
                scope.launch {
                    notifiKations.schedule(
                        notificationType = NotificationType.Immediate.Custom(
                            id = "1",
                            title = "Custom notification",
                            body = "Hello from the NotifiKations.",
                            soundType = SoundType.DEFAULT,
                        ),
                    )
                    hasLocationNotification = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Send", color = Color.White)
        }
        AnimatedVisibility(hasLocationNotification) {
            AppButton(
                text = "Dismiss location notification",
                onClick = {
                    scope.launch {
                        notifiKations.cancelNotifications(
                            listOf(
                                NotificationService.COLLECTING_LOCATION_NOTIFICATION_ID.toString()
                            )
                        )
                        hasLocationNotification = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    ExpandableCard(headerText = "Custom instant notification", expanded = false) {
        AppTextField(
            value = instantNotificationTitle,
            onValueChange = { instantNotificationTitle = it },
            placeholder = "Title",
        )
        AppTextField(
            value = instantNotificationDescription,
            onValueChange = { instantNotificationDescription = it },
            placeholder = "Description",
        )
        Button(
            onClick = {
                scope.launch {
                    notifiKations.schedule(
                        notificationType = NotificationType.Immediate.Custom(
                            id = "1",
                            title = instantNotificationTitle,
                            body = instantNotificationDescription,
                            soundType = SoundType.DEFAULT,
                        ),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Send", color = Color.White)
        }
    }
    ExpandableCard(headerText = "Custom scheduled notification", expanded = false) {
        AppTextField(
            value = scheduledNotificationTitle,
            onValueChange = { scheduledNotificationTitle = it },
            placeholder = "Title",
        )
        AppTextField(
            value = scheduledNotificationDescription,
            onValueChange = { scheduledNotificationDescription = it },
            placeholder = "Description",
        )
        AppTextField(
            value = scheduledNotificationDelay.toString(),
            onValueChange = { scheduledNotificationDelay = it.toInt() },
            placeholder = "Delay seconds",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Button(
            onClick = {
                scope.launch {
                    notifiKations.schedule(
                        notificationType = NotificationType.Scheduled(
                            title = scheduledNotificationTitle,
                            body = scheduledNotificationDescription,
                            soundType = SoundType.DEFAULT,
                            delivery = Clock.System.now() + scheduledNotificationDelay.seconds,
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Send", color = Color.White)
        }
    }
}
