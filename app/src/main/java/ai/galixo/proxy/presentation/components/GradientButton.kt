package ai.galixo.proxy.presentation.components

import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GradientButton(
    text: String,
    startColor: Color,
    endColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            startColor, // Start Color
            endColor // End Color
        )
    )

    Box(
        modifier = modifier
            .clip(CatTheme.shape.button) // Clip to button shape
            .background(brush = gradientBrush) // Apply Gradient
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = text,
            style = CatTheme.typography.buttonText,
            color = CatTheme.colorScheme.onPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GradientButtonPreview() {
    CatTheme {
        GradientButton(
            text = "Button Name",
            startColor = CatTheme.colorScheme.primary,
            endColor = CatTheme.colorScheme.secondary,
            modifier = Modifier.padding(16.dp),
            onClick = {}
        )
    }
}

