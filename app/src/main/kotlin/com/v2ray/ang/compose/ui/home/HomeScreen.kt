package com.v2ray.ang.compose.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.v2ray.ang.compose.theme.CatProxyTheme
import com.v2ray.ang.compose.theme.color_light_background

@Composable
fun HomeScreen() {
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .background(color_light_background),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Home Screen",
            fontFamily = FontFamily.Cursive,
            fontSize = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    CatProxyTheme {
        HomeScreen()
    }
}