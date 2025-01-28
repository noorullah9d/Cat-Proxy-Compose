package ai.galixo.proxy.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

private var myFirebaseAnalytics: FirebaseAnalytics? = null

const val SPLASH_SCREEN = "splash_screen"
const val LANGUAGE_SCREEN = "language_screen"
const val ON_BOARDING_GET_STARTED = "on_boarding_get_started"
const val ON_BOARDING_SKIP = "on_boarding_get_skip"
const val MAIN_SCREEN = "main_screen"
const val CONNECT_PROXY = "connect_proxy"
const val CONNECTION_SUCCESS = "connection_success"
const val CONNECTION_SUCCESS_SCREEN = "connection_success_screen"
const val CONNECTION_FAILED = "connection_failed"
const val DISCONNECT_PROXY = "disconnect_proxy"
const val SERVERS_SCREEN = "servers_screen"
const val SELECT_SERVER = "select_server"
const val FAILED_TO_LOAD_SERVERS = "failed_to_load_servers"
const val GOT_EXTRA_TIME = "got_extra_time"
const val GOT_REWARD_TIME = "got_reward_time"
const val CHANGE_LANGUAGE = "change_language"
const val FEEDBACK_SCREEN = "feedback_screen"
const val SPLIT_TUNNELING_SCREEN = "split_tunneling_screen"

// in-app purchases
const val PREMIUM_SPLASH_CROSS_FIRST_OPEN = "premium_splash_cross_first_open"
const val PREMIUM_SPLASH_CROSS_RETURNING = "premium_splash_cross_returning"
const val PREMIUM_SPLASH_SUBSCRIBE_FIRST_OPEN = "premium_splash_subscribe_first_open"
const val PREMIUM_SPLASH_SUBSCRIBE_RETURNING = "premium_splash_subscribe_returning"

const val PREMIUM_CONNECTION_CROSS = "premium_connection_cross"
const val PREMIUM_CONNECTION_WEEKLY = "premium_connection_weekly"
const val PREMIUM_CONNECTION_MONTHLY = "premium_connection_monthly"
const val PREMIUM_CONNECTION_YEARLY = "premium_connection_yearly"

const val PREMIUM_IN_APP_CROSS = "premium_in_app_cross"
const val PREMIUM_IN_APP_WEEKLY = "premium_in_app_weekly"
const val PREMIUM_IN_APP_MONTHLY = "premium_in_app_monthly"
const val PREMIUM_IN_APP_YEARLY = "premium_in_app_yearly"

fun Context.postAnalytics(event: String) {
    if (myFirebaseAnalytics == null) {
        myFirebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
    }
    val bundle = Bundle()
    bundle.putString(event, event)
    myFirebaseAnalytics?.logEvent(event, bundle)
}