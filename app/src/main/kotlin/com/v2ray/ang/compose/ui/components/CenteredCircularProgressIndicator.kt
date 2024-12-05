package com.v2ray.ang.compose.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CenteredCircularProgressIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize() // Make the Box fill the parent container
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(40.dp) // Set size of the progress indicator
                .align(Alignment.Center) // Align it in the center of the Box
        )
    }
}
