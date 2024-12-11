package com.galixo.proxy.compose.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galixo.proxy.BuildConfig
import com.galixo.proxy.R
import com.galixo.proxy.compose.theme.color_light_divider

@Composable
fun DrawerHeader(
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(218.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally, // Align content horizontally in the center
            verticalArrangement = Arrangement.Center // Align content vertically in the center
        ) {
            // App Icon
            Image(
                painter = painterResource(id = R.drawable.app_icon), // Replace with your app icon resource
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp) // Adjust the size of the app icon
                    .padding(8.dp)
            )

            // App Version Text
            Spacer(modifier = Modifier.height(4.dp)) // Spacer between app name and version text
            Text(
                text = "Version: ${BuildConfig.VERSION_NAME}", // You can dynamically set this by getting the version from the app metadata
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.7f) // Optional: slightly faded version text
                )
            )
            Spacer(modifier = Modifier.height(8.dp)) // Spacer between app name and version text
            HorizontalDivider(
                color = color_light_divider, // Set the divider color
                thickness = 1.dp, // Set the thickness of the divider
                modifier = Modifier.padding(top = 4.dp) // Optional: Add some padding
            )
        }

        // Close Drawer button (cross icon) in the top-right corner
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd) // Aligns the button to the top-right corner
                .padding(8.dp) // Add padding to ensure it doesn't overlap the content
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_close), // Replace with your cross drawable
                contentDescription = "Close Drawer"
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun DrawerHeaderPreview() {
    DrawerHeader(
        onClose = { /* Do nothing for preview */ }
    )
}
