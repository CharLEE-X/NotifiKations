[![](https://jitpack.io/v/CharLEE-X/notifiKations.svg)](https://jitpack.io/#CharLEE-X/notifiKations)
![Kotlin Version](https://img.shields.io/badge/Kotlin-1.8.20-orange)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/CharLEE-X/notifiKations)](https://github.com/CharLEE-X/notifiKations/releases)


# NotifiKations

NotifiKations is a Kotlin Multiplatform library for Android and iOS that allows you to send local
and push notifications. It provides a unified interface to initialize, send, and cancel
notifications on both platforms.

## Features

- Initialize and send notifications of various types.
- Cancel notifications by their IDs.
- Check the current state of notification permission.
- Monitor permission state changes using a Flow.
- Request and provide notification permission.
- Open the settings page for notification permissions.

## Installation

To use NotifiKations in your project, follow these steps:

1. Add the NotifiKations dependency to your project.

**Gradle:**

```kotlin
implementation("com.charleex.notifikations:notifikations:1.0.0")
```

2. Configure the necessary permissions in your project.

- For Android, make sure you have the required permissions defined in your AndroidManifest.xml file.
- For iOS, configure the necessary permissions in your project's Info.plist file.

3. Start using NotifiKations in your code by initializing the NotificationService and using its
   methods
   to send and manage notifications.

## Usage

### Initialization

To start using NotifiKations, initialize the NotificationService as follows:

```kotlin
val notifiKations = NotifiKations()
```

### Sending a Notification

To send a notification, use the schedule method and provide the desired NotificationType:

```kotlin
val notificationType = NotificationType("my_notification")
val result = notifiKations.schedule(notificationType)
if (result != null) {
    // Notification sent successfully
} else {
    // Failed to send notification
}
```

### Canceling Notifications

To cancel notifications by their IDs, use the cancelNotifications method:

```kotlin
val notificationIds = listOf("notification1", "notification2")
notifiKations.cancelNotifications(notificationIds)
```

### Checking Permission State

To check the current state of a notification permission, use the checkPermission method:

```kotlin
val permission = Permission("my_permission")
val permissionState = notifiKations.checkPermission(permission)
// Use permissionState to determine the current state of the permission
```

### Monitoring Permission State Changes
You can monitor the permission state changes using a Flow. Use the permissionState method to obtain
a Flow<PermissionState>:

```kotlin
val permission = Permission("my_permission")
notifiKations.permissionState(permission).collect { permissionState ->
    // Handle permission state changes
}
```
### Requesting Notification Permission
To request the application to provide a notification permission, use the providePermission method:

```kotlin
val permission = Permission("my_permission")
notifiKations.providePermission(permission)

### Opening Notification Settings
To open the settings page for a notification permission, use the openSettingPage method:

```kotlin
val permission = Permission("my_permission")
notifiKations.openSettingPage(permission)
```

## License
NotifiKations is licensed under the Apache 2.0 license. See the [LICENSE](LICENCE.md) file for more details.

> Note: This README provides a brief overview of the NotifiKations library. For more detailed usage
instructions and examples, please refer to the project's documentation and sample code.

For bug reports, feature requests, and [contributions](CONTRIBUTIONG.md), please visit the NotifiKations [GitHub
repository](https://github.com/CharLEE-X/notifiKations).
