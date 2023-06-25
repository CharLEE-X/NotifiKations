/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.charleex.notifikations.NotifiKations
import com.charleex.notifikations.model.Permission
import com.charleex.notifikations.model.PermissionState
import com.charleex.notifikations.model.notGranted
import com.charleex.notifikations.ui.components.AppButton
import kotlinx.coroutines.launch

@Composable
internal fun PermissionsContent(notifiKations: NotifiKations) {
    val scope = rememberCoroutineScope()

    Column {
        Text(
            text = "Permissions",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onSurface,
        )
        Divider()
    }
    Permission.values().forEach { permission ->
        val permissionState by notifiKations.permissionState(permission)
            .collectAsState(notifiKations.checkPermission(permission))
        StateSection(
            name = permission.name,
            state = permissionState,
            buttonShowing = permissionState.notGranted(),
            onRequestPermissionClick = {
                scope.launch {
                    notifiKations.providePermission(permission)
                }
            },
            onOpenSettingsClick = {
                notifiKations.openSettingPage(permission)
            }
        )
    }
}

@Composable
private fun StateSection(
    name: String,
    state: PermissionState,
    buttonText: String = "Request $name",
    buttonShowing: Boolean = true,
    onRequestPermissionClick: () -> Unit,
    onOpenSettingsClick: () -> Unit,
) {
    val colorState by animateColorAsState(
        when (state) {
            PermissionState.GRANTED -> Color.Green
            PermissionState.NOT_DETERMINED -> Color.Gray
            PermissionState.DENIED -> Color.Red
        }
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier.background(MaterialTheme.colors.surface)
                )
            }
            Icon(
                imageVector = when (state) {
                    PermissionState.GRANTED -> Icons.Default.Check
                    PermissionState.NOT_DETERMINED -> Icons.Outlined.QuestionMark
                    PermissionState.DENIED -> Icons.Outlined.Close
                },
                tint = colorState,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            AppButton(
                text = "Settings",
                onClick = onOpenSettingsClick,
                modifier = Modifier.width(100.dp)
            )
        }
        AnimatedVisibility(buttonShowing) {
            AppButton(
                text = buttonText,
                onClick = onRequestPermissionClick,
            )
        }
    }
}
