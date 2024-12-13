package com.galixo.proxy.compose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.galixo.proxy.compose.theme.CatProxyTheme
import com.galixo.proxy.compose.theme.color_light_primary

@Composable
fun LoadingDialog(onDismissRequest: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)) // Translucent overlay
    ) {
        Dialog(
            onDismissRequest = onDismissRequest // Or leave empty to make not dismissible
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp) // Set size of the progress indicator
                    .align(Alignment.Center), // Align it in the center of the Box
                color = color_light_primary
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DialogPreview() {
    CatProxyTheme {
        LoadingDialog { }
    }
}
