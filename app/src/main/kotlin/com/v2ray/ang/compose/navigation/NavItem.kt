package com.v2ray.ang.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.v2ray.ang.R

data class NavItem(
    val title: String,
    val icon: Int,
    val route: String
)

@Composable
fun getNavItems(): List<NavItem> {
    return listOf(
        NavItem(
            title = stringResource(id = R.string.language),
            icon = R.drawable.ic_languages,
            route = Destinations.LanguageScreen.route
        ),
        NavItem(
            title = stringResource(id = R.string.manage_subscription),
            icon = R.drawable.ic_restore_purchase,
            route = Destinations.ManageSubscription.route
        ),
        NavItem(
            title = stringResource(id = R.string.split_tunneling),
            icon = R.drawable.ic_filter,
            route = Destinations.SplitTunnelingScreen.route
        ),
        NavItem(
            title = stringResource(id = R.string.feedback),
            icon = R.drawable.ic_feedback,
            route = Destinations.FeedbackScreen.route
        ),
        NavItem(
            title = stringResource(id = R.string.privacy),
            icon = R.drawable.ic_privacy,
            route = Destinations.PrivacyPolicy.route
        ),
        NavItem(
            title = stringResource(id = R.string.about),
            icon = R.drawable.ic_about,
            route = Destinations.AboutScreen.route
        )
    )
}