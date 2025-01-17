package ai.galixo.proxy.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SeamlessIndeterminateProgressIndicator(
    modifier: Modifier = Modifier,
    trackColor: Color = Color.Gray,
    indicatorColor: Color = Color.Blue,
    cornerRadius: Dp = 8.dp,
    animationDuration: Int = 1000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    // Animated offset to move the indicator
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = -0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = LinearEasing
            )
        ),
        label = ""
    )

    val cornerRadiusPx = with(LocalDensity.current) { cornerRadius.toPx() }

    Box(
        modifier = modifier
            .height(8.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .background(trackColor)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val totalWidth = size.width
            val indicatorWidth = totalWidth * 0.3f // Indicator width is 30% of the total width

            // Adjust the start and end offset for seamless entry and exit
            val effectiveOffset = animatedOffset * (totalWidth + indicatorWidth) - indicatorWidth

            val visibleStart = effectiveOffset.coerceAtLeast(0f)
            val visibleEnd = (effectiveOffset + indicatorWidth).coerceAtMost(totalWidth)

            if (visibleEnd > visibleStart) {
                // Draw the moving indicator
                drawRoundRect(
                    color = indicatorColor,
                    topLeft = Offset(x = visibleStart, y = 0f),
                    size = Size(visibleEnd - visibleStart, size.height),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                )
            }
        }
    }
}