package ai.galixo.proxy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ai.galixo.proxy.R

data class NavItem(
    val title: String,
    val icon: Int,
    val route: String
)

data class GroupedNavItems(
    val groupName: String,
    val items: List<NavItem>
)

/*@Composable
fun getNavItems(): List<NavItem> {
    return listOf(
        NavItem(
            title = stringResource(id = R.string.restore_purchase),
            icon = R.drawable.ic_restore_purchase,
            route = Destinations.ManageSubscription.route
        ),
        NavItem(
            title = stringResource(id = R.string.split_tunneling),
            icon = R.drawable.ic_split_tunneling,
            route = Destinations.SplitTunnelingScreen.route
        ),
        NavItem(
            title = stringResource(id = R.string.check_current_location),
            icon = R.drawable.ic_location,
            route = Destinations.CurrentLocationScreen.route
        ),
        NavItem(
            title = stringResource(id = R.string.language),
            icon = R.drawable.ic_languages,
            route = Destinations.LanguageSheet.route
        ),
        NavItem(
            title = stringResource(id = R.string.share_with_friends),
            icon = R.drawable.ic_share,
            route = Destinations.ShareApp.route
        ),
        NavItem(
            title = stringResource(id = R.string.rate_us),
            icon = R.drawable.ic_star,
            route = Destinations.RateUs.route
        ),
        NavItem(
            title = stringResource(id = R.string.feedback),
            icon = R.drawable.ic_feedback,
            route = Destinations.FeedbackScreen.route
        ),
        NavItem(
            title = stringResource(id = R.string.faqs),
            icon = R.drawable.ic_help,
            route = Destinations.FAQsScreen.route
        )
    )
}*/

@Composable
fun getGroupedNavItems(): List<GroupedNavItems> {
    return listOf(
        GroupedNavItems(
            groupName = stringResource(id = R.string.subscription_settings),
            items = listOf(
                NavItem(
                    title = stringResource(id = R.string.restore_purchase),
                    icon = R.drawable.ic_restore_purchase,
                    route = Destinations.ManageSubscription.route
                )
            )
        ),
        GroupedNavItems(
            groupName = stringResource(id = R.string.advanced_settings),
            items = listOf(
                NavItem(
                    title = stringResource(id = R.string.split_tunneling),
                    icon = R.drawable.ic_split_tunneling,
                    route = Destinations.SplitTunnelingScreen.route
                ),
                NavItem(
                    title = stringResource(id = R.string.check_current_location),
                    icon = R.drawable.ic_location,
                    route = Destinations.CurrentLocationScreen.route
                ),
                NavItem(
                    title = stringResource(id = R.string.language),
                    icon = R.drawable.ic_languages,
                    route = Destinations.LanguageSheet.route
                )
            )
        ),
        GroupedNavItems(
            groupName = stringResource(id = R.string.others),
            items = listOf(
                NavItem(
                    title = stringResource(id = R.string.share_with_friends),
                    icon = R.drawable.ic_share,
                    route = Destinations.ShareApp.route
                ),
                NavItem(
                    title = stringResource(id = R.string.rate_us),
                    icon = R.drawable.ic_star,
                    route = Destinations.RateUs.route
                ),
                NavItem(
                    title = stringResource(id = R.string.feedback),
                    icon = R.drawable.ic_feedback,
                    route = Destinations.FeedbackScreen.route
                ),
                NavItem(
                    title = stringResource(id = R.string.faqs),
                    icon = R.drawable.ic_help,
                    route = Destinations.FAQsScreen.route
                )
            )
        )
    )
}