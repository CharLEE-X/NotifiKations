/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.model
/**
 * The SoundType enum class represents the types of sound for notifications.
 */
enum class SoundType {
    /**
     * The DEFAULT sound type plays the default notification sound.
     */
    DEFAULT,

    /**
     * The CRITICAL sound type plays a critical notification sound.
     */
    CRITICAL,

    /**
     * The NONE sound type indicates no sound should be played for the notification.
     */
    NONE
}
