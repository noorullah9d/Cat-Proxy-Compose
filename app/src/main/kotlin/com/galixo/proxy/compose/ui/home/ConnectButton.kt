package com.galixo.proxy.compose.ui.home

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galixo.proxy.R
import com.galixo.proxy.compose.admob.SmallNativeAd
import com.galixo.proxy.compose.theme.CatProxyTheme
import com.galixo.proxy.compose.theme.color_light_primary
import com.galixo.proxy.compose.theme.color_light_text_primary

@Composable
fun ConnectButton(
    context: Context,
    onClick: () -> Unit
) {
    // Remember the infinite transition for the animation
    val transition = rememberInfiniteTransition(label = "")

    // Zoom in/out animation for scaling
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // Column to stack the button and text
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 48.dp) // Adjust the bottom padding for the Column
    ) {
        // Button container
        Box(
            modifier = Modifier
                .size(75.dp)
                .clip(CircleShape)
                .background(color_light_primary)
                .clickable(onClick = { onClick() })
        ) {
            // Animated icon inside the button
            Image(
                painter = painterResource(id = R.drawable.ic_paw), // Replace with your desired icon
                contentDescription = "Connect Button",
                modifier = Modifier
                    .scale(scale)
                    .align(Alignment.Center)
                    .size(38.dp)
            )
        }

        // Spacer to create space between the button and the text (optional)
        Spacer(modifier = Modifier.height(4.dp))

        // Text below the ConnectButton
        Text(
            text = "Tap to connect",  // Your desired text
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp),
            color = color_light_text_primary
        )

        SmallNativeAd(
            context = context,
            adUnitId = "ca-app-pub-3940256099942544/2247696110"
        )
    }
}


@Composable
@Preview(showBackground = false)
fun AnimatedButtonPreview() {
    CatProxyTheme {
        val context = LocalContext.current
        ConnectButton(context) {}
    }
}