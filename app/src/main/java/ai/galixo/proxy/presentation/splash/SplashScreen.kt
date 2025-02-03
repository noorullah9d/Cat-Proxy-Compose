package ai.galixo.proxy.presentation.splash

import ai.galixo.proxy.R
import ai.galixo.proxy.admob.AppOpenManager
import ai.galixo.proxy.admob.admob_interstitial_id_splash
import ai.galixo.proxy.admob.admob_native_id_languages
import ai.galixo.proxy.admob.loadAdmobInterstitial
import ai.galixo.proxy.admob.nativeAd.NativeAd
import ai.galixo.proxy.admob.requestConsentForm
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.presentation.components.CountdownTimer
import ai.galixo.proxy.presentation.components.LottieAnimation
import ai.galixo.proxy.presentation.main.getActivity
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.DateManager.getDayOffset
import ai.galixo.proxy.utils.extensions.isOnline
import ai.galixo.proxy.utils.isShowingAd
import ai.galixo.proxy.utils.shouldShowInterstitialAd
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieConstants
import com.github.shadowsocks.preference.PrefUtils
import com.github.shadowsocks.preference.PrefUtils.isAdsRemoved
import com.github.shadowsocks.preference.PrefUtils.isAppOpenAllowedFromRemoteConfig
import com.github.shadowsocks.preference.PrefUtils.isSplashInterstitialAllowedFromRemoteConfig
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import timber.log.Timber

@Composable
fun SplashScreen(
    appOpenManager: AppOpenManager?,
    viewModel: SplashViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val activity = getActivity()
    val view = LocalView.current
    val isAdsRemoved = remember { isAdsRemoved }
    val isOnline = remember { context.isOnline() }
//    var isAnyAdShown by remember { mutableStateOf(false) }
    val interstitialAd = remember { mutableStateOf<InterstitialAd?>(null) }
    var showPremiumCountdown by remember { mutableStateOf(false) }
    var showAdCountdown by remember { mutableStateOf(false) }

    // Handle Consent & Ads
    LaunchedEffect(Unit) {
        if (isOnline && !isAdsRemoved) {
            activity?.requestConsentForm {
                // Load Ads only after consent is completed
                /*if (!viewModel.isLanguageShown()) {
                    NativeAd.loadAd(activity, admob_native_id_languages)
                }*/

                if (shouldShowInterstitialAd) {
                    if (isSplashInterstitialAllowedFromRemoteConfig) {
                        Timber.d("SplashScreen: Loading interstitial ad")
                        context.loadAdmobInterstitial(
                            admob_interstitial_id_splash,
                            onAdLoaded = { interstitialAd.value = it },
                            onAdFailedToLoad = { Timber.d("SplashScreen: Failed to load interstitial: ${it.message}") }
                        )
                        showAdCountdown = true
                    } else {
                        showPremiumCountdown = true
                    }
                } else {
                    if (isAppOpenAllowedFromRemoteConfig) {
                        appOpenManager?.fetchAd()
                        showAdCountdown = true
                    } else {
                        shouldShowInterstitialAd = true
                        showPremiumCountdown = true
                    }
                }
            }
        } else {
            showPremiumCountdown = true
        }
    }

    // Change status bar for PremiumScreen
    DisposableEffect(Unit) {
        val window = (view.context as Activity).window

        // Apply premium screen status bar
        window.statusBarColor = Color.Transparent.toArgb() // Make it transparent
        WindowCompat.setDecorFitsSystemWindows(window, false) // Extend content into status bar
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false // White icons

        // Reset status bar when PremiumScreen is removed
        onDispose {
            window.statusBarColor = Color.White.toArgb() // Restore default
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true // Dark icons
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SplashContent()

        // Timers for Ad / Premium Users
        if (showAdCountdown) {
            StartAdCountDownTimer(activity, interstitialAd, appOpenManager) {
                handleNavigation(viewModel, navController)
            }
        }
        if (showPremiumCountdown) {
            StartPremiumCountDownTimer {
                handleNavigation(viewModel, navController)
            }
        }
    }
}

@Composable
fun StartPremiumCountDownTimer(
    onNavigate: () -> Unit
) {
    CountdownTimer(
        waitingTime = 3000L,
        isCancelled = false,
        onTick = { Timber.d("Premium Timer: $it ms remaining") },
        onFinish = {
            Timber.d("Premium Timer Finished!")
            onNavigate.invoke()
        }
    )
}

@Composable
fun StartAdCountDownTimer(
    activity: Activity?,
    interstitialAd: MutableState<InterstitialAd?>,
    appOpenManager: AppOpenManager?,
    onNavigate: () -> Unit
) {
    var isCancelled by remember { mutableStateOf(false) }

    CountdownTimer(
        waitingTime = 12000L,
        isCancelled = isCancelled,
        onTick = { millisRemaining ->
            Timber.d("Ad Countdown: $millisRemaining ms remaining")

            if (millisRemaining <= 9000 && !isCancelled) {
                if (!shouldShowInterstitialAd) {
                    appOpenManager?.showAdIfAvailable(
                        onAdShown = {
                            Timber.d("App Open Ad Started: Cancelling Timer")
                            if (!isCancelled) isCancelled = true // Stop timer immediately when ad starts
                        },
                        onDismissed = {
                            Timber.d("App Open Ad Dismissed")
                            onNavigate.invoke()
                        }
                    )
                } else {
                    activity?.let {
                        interstitialAd.value?.show(it,
                            onAdShown = {
                                if (!isCancelled) isCancelled = true // Stop timer immediately
                            },
                            onDismissed = {
                                Timber.d("Interstitial Ad Shown: Cancelling Timer")
                                onNavigate.invoke()
                            }
                        )
                    }
                }
            }
        },
        onFinish = {
            if (!isCancelled) {
                Timber.d("Ad Countdown Finished!")
                onNavigate.invoke()
            }
        }
    )
}

fun InterstitialAd?.show(
    activity: Activity,
    onDismissed: () -> Unit,
    onAdShown: () -> Unit
) {
    this?.fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdImpression() {
            super.onAdImpression()
            isShowingAd = true
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            isShowingAd = true
            onAdShown.invoke()
        }

        override fun onAdDismissedFullScreenContent() {
            onDismissed.invoke()
            isShowingAd = false
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
            onDismissed.invoke()
            isShowingAd = false
        }
    }
    this?.show(activity)
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