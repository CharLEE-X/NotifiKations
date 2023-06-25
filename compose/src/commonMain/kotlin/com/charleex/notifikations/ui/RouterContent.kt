package com.charleex.notifikations.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charleex.notifikations.ui.theme.ApplicationTheme
import com.charleex.notifikations.ui.theme.Dimensions.BOTTOM_BAR_HEIGHT
import com.charleex.notifikations.ui.theme.Dimensions.TOP_BAR_HEIGHT
import com.charleex.notifikations.ui.theme.safePaddingValues

@Composable
internal fun RouterContent() {
    val notifiKations = provideNotifiKations()

    ApplicationTheme {
        Surface(
            color = MaterialTheme.colors.surface,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = (TOP_BAR_HEIGHT + 16).dp,
                        bottom = (16 + BOTTOM_BAR_HEIGHT + safePaddingValues.calculateBottomPadding().value).dp
                    )
            ) {
                item {
                    PermissionsContent(notifiKations)
                }
                item {
                    Divider()
                }
                item {
                    NotificationsContent(notifiKations)
                }
            }
        }
    }
}
