/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

package com.charleex.notifikations.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("FunctionName")
@ExperimentalMaterialApi
@Composable
internal fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    shape: Shape = MaterialTheme.shapes.small,
    leadingIcon: ImageVector? = null,
    isSecure: Boolean = false,
    backgroundColor: Color = MaterialTheme.colors.surface,
    textColor: Color = MaterialTheme.colors.onSurface,
    contentColor: Color = if (isSystemInDarkTheme()) Color.DarkGray else Color.Gray,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    /**
     * Normally we would take value for the textField from "text", but there is a bug in iOS,
     * and TextField updates changed value from the state with a delay.
     * This is a workaround to use mutableStateOf as a source of truth, and mirroring it
     * to the state using LaunchEffect.
     */
    var textFieldValue by remember { mutableStateOf(value) }

    LaunchedEffect(textFieldValue) {
        onValueChange(textFieldValue)
    }

    val focusRequester = FocusRequester()
    var focusState by remember { mutableStateOf(false) }
    var visualTransformationState by remember {
        mutableStateOf(
            if (isSecure) PasswordVisualTransformation() else VisualTransformation.None
        )
    }

    val visualTransformationIcon =
        if (visualTransformationState is PasswordVisualTransformation)
            Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    val scaleState by animateFloatAsState(
        targetValue = if (focusState) 1.02f else 1f,
        animationSpec = tween(500)
    )
    val backgroundColorState by animateColorAsState(
        targetValue = if (focusState)
            backgroundColor.copy(alpha = .5f) else backgroundColor,
        animationSpec = tween(500)
    )
    var wasAutoFilled by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            wasAutoFilled = false
        },
        placeholder = {
            Text(
                text = placeholder,
                color = textColor,
                modifier = Modifier.alpha(.5f)
            )
        },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier
                )
            }
        },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(isSecure && textFieldValue.isNotEmpty()) {
                    Surface(
                        onClick = {
                            visualTransformationState = visualTransformationState.toggle()
                        },
                        color = Color.Transparent,
                    ) {
                        Icon(
                            imageVector = visualTransformationIcon,
                            contentDescription = "Toggle password visibility",
                            tint = contentColor,
                        )
                    }
                }
                AnimatedVisibility(
                    textFieldValue.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Surface(
                        onClick = {
                            textFieldValue = ""
                            onValueChange("")
                            wasAutoFilled = false
                        },
                        color = Color.Transparent,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear text input",
                            tint = contentColor,
                        )
                    }
                }
            }
        },
        shape = shape,
        singleLine = true,
        maxLines = 1,
        visualTransformation = visualTransformationState,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = MaterialTheme.typography.body2.copy(color = textColor),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = if (wasAutoFilled) Color(0xFFFFFDE7) else backgroundColorState,
        ),
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState = it.isFocused }
            .graphicsLayer {
                scaleX = scaleState
                scaleY = scaleState
            }
    )
}

private fun VisualTransformation.toggle() = if (this is PasswordVisualTransformation) {
    VisualTransformation.None
} else {
    PasswordVisualTransformation()
}
