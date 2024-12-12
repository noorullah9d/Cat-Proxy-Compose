package com.galixo.proxy.compose.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.galixo.proxy.compose.theme.color_light_primary
import com.galixo.proxy.compose.theme.color_light_unselected

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    thumbColor: Color = Color.White,
    thumbUncheckedColor: Color = Color.White,
    trackCheckedColor: Color = MaterialTheme.colorScheme.primary,
    trackUncheckedColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    trackWidth: Dp = 50.dp, // Width of the track
    trackHeight: Dp = 28.dp, // Height of the track
    thumbDiameterFraction: Float = 0.75f, // Diameter of the thumb as a fraction of trackHeight
    paddingFraction: Float = 0.15f // Padding as a fraction of trackHeight
) {
    // Dynamically calculate sizes
    val thumbSize = trackHeight * thumbDiameterFraction
    val padding = trackHeight * paddingFraction
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - padding else padding,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Box(
        modifier = modifier
            .width(trackWidth)
            .height(trackHeight)
            .clip(CircleShape)
            .background(if (checked) trackCheckedColor else trackUncheckedColor)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset(x = thumbOffset)
                .clip(CircleShape)
                .background(if (checked) thumbColor else thumbUncheckedColor)
        )
    }
}

@Preview
@Composable
fun CustomSwitchPreview() {
    var isChecked by remember { mutableStateOf(true) }

    CustomSwitch(
        checked = isChecked,
        onCheckedChange = { isChecked = it },
        thumbColor = Color.White,
        thumbUncheckedColor = Color.White,
        trackCheckedColor = color_light_primary,
        trackUncheckedColor = color_light_unselected
    )
}

