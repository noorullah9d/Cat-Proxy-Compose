package ai.galixo.proxy.navigation

import ai.galixo.proxy.presentation.faqs.FAQsScreen
import ai.galixo.proxy.presentation.splash.AgreementScreen
import ai.galixo.proxy.presentation.feedback.FeedbackScreen
import ai.galixo.proxy.presentation.home.HomeScreen
import ai.galixo.proxy.presentation.home.HomeViewModel
import ai.galixo.proxy.presentation.iap.PremiumScreen
import ai.galixo.proxy.presentation.languages.LanguageScreen
import ai.galixo.proxy.presentation.location.LocationScreen
import ai.galixo.proxy.presentation.servers.ServersScreen
import ai.galixo.proxy.presentation.splash.SplashScreen
import ai.galixo.proxy.presentation.tunneling.SplitTunnelingScreen
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(
    context: Context,
    viewModel: HomeViewModel,
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
                    Destinations.LanguageScreen -> LanguageScreen(
                        navController = navController
                    )

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

                    Destinations.HomeScreen -> {
                        HomeScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }

                    Destinations.ManageSubscription -> {
                        OpenLink("https://www.google.com", navController)
                    }

                    Destinations.SplitTunnelingScreen -> SplitTunnelingScreen(
                        onBackPressed = { navController.popBackStack() }
                    )

                    Destinations.CurrentLocationScreen -> LocationScreen {
                        navController.popBackStack()
                    }

                    Destinations.LanguageSheet -> {}

                    Destinations.ShareApp -> {}

                    Destinations.RateUs -> {}

                    Destinations.FeedbackScreen -> FeedbackScreen {
                        navController.popBackStack()
                    }

                    Destinations.PrivacyPolicy -> {
                        OpenLink("https://www.google.com", navController)
                    }

                    Destinations.FAQsScreen -> FAQsScreen {
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