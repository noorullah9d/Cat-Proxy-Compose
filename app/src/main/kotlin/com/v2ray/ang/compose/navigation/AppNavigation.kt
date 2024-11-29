package com.v2ray.ang.compose.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.v2ray.ang.compose.ui.AboutScreen
import com.v2ray.ang.compose.ui.SplitTunnelingScreen
import com.v2ray.ang.compose.ui.home.HomeScreen
import com.v2ray.ang.compose.ui.FeedbackScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = DrawerDestinations.HomeScreen.route) {
        DrawerDestinations.entries.forEach { screen ->
            composable(screen.route) {
                when (screen) {
                    DrawerDestinations.HomeScreen -> HomeScreen()
                    DrawerDestinations.LanguageScreen -> {}
                    DrawerDestinations.ManageSubscription -> {
                        OpenLink("https://www.google.com", navController)
                    }
                    DrawerDestinations.SplitTunnelingScreen -> SplitTunnelingScreen()
                    DrawerDestinations.FeedbackScreen -> FeedbackScreen()
                    DrawerDestinations.PrivacyPolicy -> {
                        OpenLink("https://www.google.com", navController)
                    }
                    DrawerDestinations.AboutScreen -> AboutScreen {
                        navController.popBackStack()
                    }
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
    navController.popBackStack(DrawerDestinations.HomeScreen.route, inclusive = false)
}