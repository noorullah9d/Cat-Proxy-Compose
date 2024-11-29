package com.v2ray.ang.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.v2ray.ang.compose.theme.CatProxyTheme
import com.v2ray.ang.compose.theme.color_light_background
import com.v2ray.ang.compose.ui.components.CustomTopAppBar

@Composable
fun AboutScreen(onBackPressed: () -> Unit) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "About",
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
        // Your content goes here (app icon, version, etc.)
    }
}

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    CatProxyTheme {
        AboutScreen {}
    }
}