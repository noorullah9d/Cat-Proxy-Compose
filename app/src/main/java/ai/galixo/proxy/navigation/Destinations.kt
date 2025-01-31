package ai.galixo.proxy.navigation

enum class Destinations(val route: String) {
    LanguageScreen("language"),
    AgreementScreen("agreement"),
    SplashScreen("splash"),
    HomeScreen("home"),
    LanguageSheet("language_sheet"),
    ManageSubscription("manage_subscription"),
    SplitTunnelingScreen("split_tunneling"),
    CurrentLocationScreen("current_location"),
    ShareApp("share_app"),
    RateUs("rate_us"),
    FeedbackScreen("feedback"),
    FAQsScreen("faqs"),
    PremiumScreen("premium/{source}"),
    FreeTrialScreen("free_trial"),
    ServersScreen("servers")
}
