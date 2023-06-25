/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.model

import com.benasher44.uuid.uuid4
import kotlinx.datetime.Instant

/**
 * The NotificationType interface represents the types of notifications
 * that can be sent.
 */
sealed interface NotificationType {
    /**
     * The Immediate sealed interface represents immediate notifications.
     */
    sealed interface Immediate : NotificationType {
        /**
         * The Custom data class represents a custom immediate notification type.
         * @param id The unique identifier for the notification.
         * @param title The title of the notification.
         * @param body The body/content of the notification.
         * @param soundType The type of sound to play for the notification.
         */
        data class Custom(
            val id: String,
            val title: String,
            val body: String,
            val soundType: SoundType = SoundType.DEFAULT,
        ) : Immediate
    }

    /**
     * The Scheduled data class represents scheduled notifications.
     * @param id The unique identifier for the notification.
     * @param title The title of the notification.
     * @param body The body/content of the notification.
     * @param delivery The scheduled delivery time of the notification.
     * @param soundType The type of sound to play for the notification.
     */
    data class Scheduled(
        val id: String = uuid4().toString(),
        val title: String,
        val body: String,
        val delivery: Instant,
        val soundType: SoundType = SoundType.DEFAULT,
    ) : NotificationType
}
