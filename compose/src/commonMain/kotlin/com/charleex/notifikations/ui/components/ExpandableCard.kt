package com.charleex.notifikations.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun ExpandableCard(
    modifier: Modifier = Modifier,
    headerText: String,
    headerContent: @Composable RowScope.() -> Unit = {},
    shape: Shape = MaterialTheme.shapes.medium,
    expanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    // Pressed state
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressed = updateTransition(targetState = isPressed, label = "Card")
    val cardScale by pressed.animateFloat(label = "Card scale") { state ->
        if (state) .99f else 1f
    }

    // Expanded state
    var checked by rememberSaveable { mutableStateOf(expanded) }
    val headerIconRotation by animateFloatAsState(if (checked) 180f else 0f)
    val contentAlpha by animateFloatAsState(if (checked) 1f else 0f)

    Card(
        shape = shape,
        modifier = Modifier
            .scale(cardScale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                checked = !checked
            }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Surface(
                color = MaterialTheme.colors.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 8.dp)
                    .padding(bottom = 8.dp)
                    .clickable(
                        role = Role.Button,
                        onClick = { checked = !checked }
                    )
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoText(
                        text = headerText,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Start,
                    )
                    headerContent()
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        imageVector = Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.rotate(headerIconRotation),
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
            AnimatedVisibility(checked) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .alpha(contentAlpha)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    content()
                }
            }
        }
    }
}


@Composable
internal fun InfoText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colors.onSurface,
    fontWeight: FontWeight = FontWeight.Light,
    fontSize: TextUnit = 12.sp,
    softWrap: Boolean = false,
    textAlign: TextAlign = TextAlign.Center,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle2,
        color = color,
        fontWeight = fontWeight,
        fontSize = fontSize,
        softWrap = softWrap,
        textAlign = textAlign,
        modifier = modifier,
    )
}
