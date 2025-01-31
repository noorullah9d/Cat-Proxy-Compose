package ai.galixo.proxy.navigation

import ai.galixo.proxy.presentation.faqs.FAQsScreen
import ai.galixo.proxy.presentation.feedback.FeedbackScreen
import ai.galixo.proxy.presentation.home.HomeScreen
import ai.galixo.proxy.presentation.home.HomeViewModel
import ai.galixo.proxy.presentation.iap.BillingViewModel
import ai.galixo.proxy.presentation.iap.FreeTrialScreen
import ai.galixo.proxy.presentation.iap.PremiumScreen
import ai.galixo.proxy.presentation.languages.LanguageScreen
import ai.galixo.proxy.presentation.location.LocationScreen
import ai.galixo.proxy.presentation.servers.ServersScreen
import ai.galixo.proxy.presentation.splash.AgreementScreen
import ai.galixo.proxy.presentation.splash.SplashScreen
import ai.galixo.proxy.presentation.tunneling.SplitTunnelingScreen
import ai.galixo.proxy.utils.extensions.toast
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun AppNavigation(
    context: Context,
    billingViewModel: BillingViewModel,
    viewModel: HomeViewModel,
    navController: NavHostController,
    hasTermsAndConditionsAccepted: Boolean,
    onRestart: () -> Unit
) {
    /*NavHost(
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
                            openLink(context, "https://galixo.ai/cat-proxy/privacy-policy")
                        },
                        onTermsClick = {
                            openLink(context, "https://galixo.ai/cat-proxy/terms-and-conditions")
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
                        OpenLink("https://play.google.com/store/account/subscriptions?package=${context.packageName}", navController)
                    }

                    Destinations.SplitTunnelingScreen -> SplitTunnelingScreen(
                        onBackPressed = { navController.popBackStack() }
                    )

                    Destinations.CurrentLocationScreen -> LocationScreen {
                        navController.popBackStack()
                    }

                    Destinations.LanguageSheet -> {}

                    Destinations.ShareApp -> {
                        ShareApp(navController)
                    }

                    Destinations.RateUs -> {
                        OpenLink("https://play.google.com/store/apps/details?id=${context.packageName}", navController)
                    }

                    Destinations.FeedbackScreen -> FeedbackScreen {
                        navController.popBackStack()
                    }

                    Destinations.FAQsScreen -> FAQsScreen {
                        navController.popBackStack()
                    }

                    Destinations.PremiumScreen -> PremiumScreen(
                        premiumSource = "",
                        billingViewModel = billingViewModel,
                        navController = navController,
                        onPrivacyPolicyClick = {
                            openLink(context, "https://galixo.ai/cat-proxy/privacy-policy")
                        },
                        onTermsClick = {
                            openLink(context, "https://galixo.ai/cat-proxy/terms-and-conditions")
                        },
                        onRestart = {
                            onRestart()
                        }
                    )

                    Destinations.FreeTrialScreen -> FreeTrialScreen(
                        billingViewModel = billingViewModel,
                        navController = navController,
                        onPrivacyPolicyClick = {
                            openLink(context, "https://galixo.ai/cat-proxy/privacy-policy")
                        },
                        onTermsClick = {
                            openLink(context, "https://galixo.ai/cat-proxy/terms-and-conditions")
                        },
                        onRestart = {
                            onRestart()
                        }
                    )

                    Destinations.ServersScreen -> ServersScreen(
                        homeViewModel = viewModel,
                        navController = navController,
                        onBackPressed = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }*/
    NavHost(
        navController = navController,
        startDestination = if (!hasTermsAndConditionsAccepted) Destinations.AgreementScreen.route else Destinations.SplashScreen.route
    ) {
        composable(Destinations.AgreementScreen.route) {
            AgreementScreen(
                navController = navController,
                onPrivacyPolicyClick = {
                    openLink(context, "https://galixo.ai/cat-proxy/privacy-policy")
                },
                onTermsClick = {
                    openLink(context, "https://galixo.ai/cat-proxy/terms-and-conditions")
                }
            )
        }

        composable(Destinations.SplashScreen.route) {
            SplashScreen(navController = navController)
        }

        composable(Destinations.LanguageScreen.route) {
            LanguageScreen(navController = navController)
        }

        composable(Destinations.HomeScreen.route) {
            HomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(Destinations.ManageSubscription.route) {
            OpenLink(
                "https://play.google.com/store/account/subscriptions?package=${context.packageName}",
                navController
            )
        }

        composable(Destinations.SplitTunnelingScreen.route) {
            SplitTunnelingScreen(
                onBackPressed = { navController.popBackStack() }
            )
        }

        composable(Destinations.CurrentLocationScreen.route) {
            LocationScreen {
                navController.popBackStack()
            }
        }

        composable(Destinations.LanguageSheet.route) { }

        composable(Destinations.ShareApp.route) {
            ShareApp(navController)
        }

        composable(Destinations.RateUs.route) {
            OpenLink(
                "https://play.google.com/store/apps/details?id=${context.packageName}",
                navController
            )
        }

        composable(Destinations.FeedbackScreen.route) {
            FeedbackScreen {
                navController.popBackStack()
            }
        }

        composable(Destinations.FAQsScreen.route) {
            FAQsScreen {
                navController.popBackStack()
            }
        }

        // ✅ PremiumScreen with source argument
        composable(
            route = Destinations.PremiumScreen.route,
            arguments = listOf(navArgument("source") { defaultValue = "default" })
        ) { navBackStackEntry ->
            val source = navBackStackEntry.arguments?.getString("source") ?: "default"

            PremiumScreen(
                premiumSource = source,
                billingViewModel = billingViewModel,
                navController = navController,
                onPrivacyPolicyClick = {
                    openLink(context, "https://galixo.ai/cat-proxy/privacy-policy")
                },
                onTermsClick = {
                    openLink(context, "https://galixo.ai/cat-proxy/terms-and-conditions")
                },
                onRestart = {
                    onRestart()
                }
            )
        }

        composable(Destinations.FreeTrialScreen.route) {
            FreeTrialScreen(
                billingViewModel = billingViewModel,
                navController = navController,
                onPrivacyPolicyClick = {
                    openLink(context, "https://galixo.ai/cat-proxy/privacy-policy")
                },
                onTermsClick = {
                    openLink(context, "https://galixo.ai/cat-proxy/terms-and-conditions")
                },
                onRestart = {
                    onRestart()
                }
            )
        }

        composable(Destinations.ServersScreen.route) {
            ServersScreen(
                homeViewModel = viewModel,
                navController = navController,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
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

@Composable
fun ShareApp(navController: NavHostController) {
    val context = LocalContext.current
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(
        Intent.EXTRA_TEXT,
        "Hey check out my app at: https://play.google.com/store/apps/details?id=${context.packageName}"
    )
    sendIntent.type = "text/plain"
    try {
        context.startActivity(sendIntent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        context.toast("Activity not found!")
    }

    // Pop the current screen off the stack (if not on Home), and navigate to Home screen
    // This will remove any screen above the Home screen, so it doesn't appear again when returning.
    navController.popBackStack(Destinations.HomeScreen.route, inclusive = false)
}