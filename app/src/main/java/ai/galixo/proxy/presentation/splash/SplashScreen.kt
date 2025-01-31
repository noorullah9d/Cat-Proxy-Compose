package ai.galixo.proxy.presentation.splash

import ai.galixo.proxy.R
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.presentation.components.CountdownTimer
import ai.galixo.proxy.presentation.components.LottieAnimation
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.DateManager.getDayOffset
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieConstants
import com.github.shadowsocks.preference.PrefUtils
import com.github.shadowsocks.preference.PrefUtils.isAdsRemoved
import timber.log.Timber

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navController: NavController
) {
    val waitingTime = if (isAdsRemoved) 3000L else 3000L

    CountdownTimer(
        waitingTime = waitingTime,
        onTick = { remainingTime ->
            Timber.d("SplashScreen4: onTick: $remainingTime")
        },
        onFinish = {
            handleNavigation(viewModel, navController)
            Timber.d("SplashScreen4: onFinish called!")
        }
    )

    CatTheme(navController = navController) {
        SplashContent()
    }

}

private fun handleNavigation(viewModel: SplashViewModel, navController: NavController) {
    if (shouldShowOffer() && !isAdsRemoved) {
        Timber.d("SplashFragment: show free trial screen")
        navController.navigateAndFinish(Destinations.FreeTrialScreen.route)
    } else if (!viewModel.isLanguageShown()) {
        navController.navigateAndFinish(Destinations.LanguageScreen.route)
    } else {
        navController.navigateAndFinish(Destinations.HomeScreen.route)
    }
}

private fun shouldShowOffer(): Boolean {
    val currentDayOffset = getDayOffset()
    return currentDayOffset in 1..4 && PrefUtils.lastShownOfferDay < currentDayOffset
}

private fun NavController.navigateAndFinish(route: String) {
    navigate(route) {
        popUpTo(Destinations.SplashScreen.route) { inclusive = true }
    }
}

@Composable
fun SplashContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CatTheme.colorScheme.primary)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 52.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LottieAnimation(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                animationRes = R.raw.anim_splash,
                iterations = LottieConstants.IterateForever
            )
        }

        /*SeamlessIndeterminateProgressIndicator(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .width(280.dp)
                .align(Alignment.CenterHorizontally),
            trackColor = CatTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
            indicatorColor = CatTheme.colorScheme.onPrimary,
            cornerRadius = 12.dp
        )*/
        Box(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .width(280.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            LottieAnimation(
                modifier = Modifier.fillMaxWidth(),
                animationRes = R.raw.anim_splash_loading,
                iterations = LottieConstants.IterateForever
            )
        }
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Configuring your device...",
            style = CatTheme.typography.regularText,
            color = CatTheme.colorScheme.onPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    CatTheme {
        SplashContent()
    }
}