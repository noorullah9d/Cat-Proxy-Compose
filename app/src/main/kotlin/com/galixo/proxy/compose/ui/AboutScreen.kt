package com.galixo.proxy.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galixo.proxy.BuildConfig
import com.galixo.proxy.compose.theme.CatProxyTheme
import com.galixo.proxy.compose.theme.color_light_background
import com.galixo.proxy.compose.ui.components.CustomTopAppBar
import com.galixo.proxy.R

@Composable
fun AboutScreen(onBackPressed: () -> Unit) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.about),
                onBackPressed = onBackPressed,
                menuItems = listOf()
            )
        },
        content = { paddingValues ->
            AboutContent(Modifier.padding(paddingValues))
        }
    )
}

@Composable
fun AboutContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color_light_background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_icon), // Replace with your app icon resource
            contentDescription = null,
            modifier = Modifier
                .size(170.dp) // Adjust the size of the app icon
                .padding(8.dp)
        )

        // App Version Text
        Spacer(modifier = Modifier.height(4.dp)) // Spacer between app name and version text
        Text(
            text = "Version: ${BuildConfig.VERSION_NAME}", // You can dynamically set this by getting the version from the app metadata
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.7f) // Optional: slightly faded version text
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    CatProxyTheme {
        AboutScreen {}
    }
}