package ai.galixo.proxy.presentation.iap

import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplitButton(
    modifier: Modifier,
    leftText: String,
    leftPrice: String,
    rightText: String,
    rightPrice: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(CatTheme.shape.button)
            .background(CatTheme.colorScheme.background)
    ) {
        // Left Button Section
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Cyan, // Darker at the top
                            CatTheme.colorScheme.secondary  // Lighter at the bottom
                        )
                    )
                )
                .clickable { onLeftClick() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = leftText,
                    style = CatTheme.typography.extraBold.copy(fontSize = 19.sp),
                    color = CatTheme.colorScheme.textPrimary
                )
                Text(
                    text = leftPrice,
                    style = CatTheme.typography.semiBold.copy(fontSize = 14.sp),
                    color = CatTheme.colorScheme.textPrimary
                )
            }
        }

        // Divider between buttons
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(CatTheme.colorScheme.divider)
        )

        // Right Button Section
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                .background(CatTheme.colorScheme.background)
                .clickable { onRightClick() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = rightText,
                    style = CatTheme.typography.extraBold.copy(fontSize = 19.sp),
                    color = CatTheme.colorScheme.textPrimary
                )
                Text(
                    text = rightPrice,
                    style = CatTheme.typography.semiBold.copy(fontSize = 14.sp),
                    color = CatTheme.colorScheme.textPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplitButtonPreview() {
    CatTheme {
        SplitButton(
            modifier = Modifier,
            leftText = "1 Week",
            leftPrice = "$2.49/week",
            rightText = "1 Month",
            rightPrice = "$4.99/month",
            onLeftClick = {},
            onRightClick = {}
        )
    }
}