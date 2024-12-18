package com.galixo.proxy.compose.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieConstants
import com.galixo.proxy.R
import com.galixo.proxy.compose.navigation.Destinations
import com.galixo.proxy.compose.theme.CatProxyTheme
import com.galixo.proxy.compose.theme.color_light_background
import com.galixo.proxy.compose.theme.color_light_primary
import com.galixo.proxy.compose.ui.components.LottieAnimation
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {

    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate(Destinations.HomeScreen.route) {
            popUpTo(Destinations.SplashScreen.route) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color_light_background)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 42.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color_light_background)
                .weight(1f)
        ) {
            LottieAnimation(
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.Center),
                animationRes = R.raw.anim_splash,
                iterations = LottieConstants.IterateForever
            )
        }

        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterHorizontally),
            color = color_light_primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    CatProxyTheme {
        val navController = rememberNavController()
        SplashScreen(navController)
    }
}