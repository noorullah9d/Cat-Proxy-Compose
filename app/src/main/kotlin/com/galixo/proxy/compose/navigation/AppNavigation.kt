package com.galixo.proxy.compose.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.galixo.proxy.compose.ui.AboutScreen
import com.galixo.proxy.compose.ui.AgreementScreen
import com.galixo.proxy.compose.ui.tunneling.SplitTunnelingScreen
import com.galixo.proxy.compose.ui.home.HomeScreen
import com.galixo.proxy.compose.ui.feedback.FeedbackScreen
import com.galixo.proxy.compose.ui.PremiumScreen
import com.galixo.proxy.compose.ui.servers.ServersScreen
import com.galixo.proxy.compose.ui.splash.SplashScreen

@Composable
fun AppNavigation(
    context: Context,
    navController: NavHostController,
    hasTermsAndConditionsAccepted: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (!hasTermsAndConditionsAccepted) Destinations.AgreementScreen.route else Destinations.SplashScreen.route
    ) {
        Destinations.entries.forEach { screen ->
            composable(screen.route) {
                when (screen) {
                    Destinations.AgreementScreen -> AgreementScreen(
                        navController = navController,
                        onPrivacyPolicyClick = {
                            openLink(context, "https://www.youtube.com")
                        },
                        onTermsClick = {
                            openLink(context, "https://www.yahoo.com")
                        }
                    )

                    Destinations.SplashScreen -> SplashScreen(
                        navController = navController
                    )

                    Destinations.HomeScreen -> HomeScreen(
                        navController = navController
                    )

                    Destinations.LanguageScreen -> {}

                    Destinations.ManageSubscription -> {
                        OpenLink("https://www.google.com", navController)
                    }

                    Destinations.SplitTunnelingScreen -> SplitTunnelingScreen {
                        navController.popBackStack()
                    }

                    Destinations.FeedbackScreen -> FeedbackScreen {
                        navController.popBackStack()
                    }

                    Destinations.PrivacyPolicy -> {
                        OpenLink("https://www.google.com", navController)
                    }

                    Destinations.AboutScreen -> AboutScreen {
                        navController.popBackStack()
                    }

                    Destinations.PremiumScreen -> PremiumScreen {
                        navController.popBackStack()
                    }

                    Destinations.ServersScreen -> ServersScreen(
                        onBackPressed = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OpenLink(url: String, navController: NavHostController) {
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)

    // Pop the current screen off the stack (if not on Home), and navigate to Home screen
    // This will remove any screen above the Home screen, so it doesn't appear again when returning.
    navController.popBackStack(Destinations.HomeScreen.route, inclusive = false)
}

fun openLink(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}