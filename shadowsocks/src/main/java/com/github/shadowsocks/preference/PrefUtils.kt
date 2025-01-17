package com.github.shadowsocks.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.github.shadowsocks.preference.Constants.APP_PREFS
import com.github.shadowsocks.preference.Constants.BROWSER_DOWNLOAD
import com.github.shadowsocks.preference.Constants.DISABLED_APPS
import com.github.shadowsocks.preference.Constants.FREE_VPN_TIME
import com.github.shadowsocks.preference.Constants.FREE_VPN_TIME_VALUE
import com.github.shadowsocks.preference.Constants.HAS_TERM_AND_CONDITION_ACCEPTED
import com.github.shadowsocks.preference.Constants.IS_APP_OPEN_ALLOWED_FROM_REMOTE_CONFIG
import com.github.shadowsocks.preference.Constants.IS_BROWSER_GUIDE_SHOWN
import com.github.shadowsocks.preference.Constants.IS_LANGUAGES_SHOWN
import com.github.shadowsocks.preference.Constants.IS_ONBOARDING_SHOWN
import com.github.shadowsocks.preference.Constants.IS_PREMIUM
import com.github.shadowsocks.preference.Constants.IS_SPLASH_INTERSTITIAL_ALLOWED_FROM_REMOTE_CONFIG
import com.github.shadowsocks.preference.Constants.IS_SPLASH_PREMIUM_FIRST_VISIT
import com.github.shadowsocks.preference.Constants.KEY_FIRST_LAUNCH_DATE
import com.github.shadowsocks.preference.Constants.KEY_LAST_SHOWN_OFFER_DAY
import com.github.shadowsocks.preference.Constants.LAST_PREMIUM_SHOWN_TIME
import com.github.shadowsocks.preference.Constants.LAST_REWARD_TIME
import com.github.shadowsocks.preference.Constants.LAST_SELECTED_SERVER
import com.github.shadowsocks.preference.Constants.SELECTED_LANG
import com.github.shadowsocks.preference.Constants.SELECTED_SERVER
import com.github.shadowsocks.preference.Constants.SELECTED_SERVER_PROXY
import com.github.shadowsocks.preference.Constants.SELECTED_SERVICE_MODE
import com.github.shadowsocks.preference.Constants.SESSION_COUNT
import com.github.shadowsocks.preference.Constants.SHOULD_SHOW_REPORT
import com.github.shadowsocks.utils.Key
import com.google.gson.Gson

object PrefUtils {
    private lateinit var sharedPreferences: SharedPreferences
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
    }

    var lastShownOfferDay: Int
        get() = sharedPreferences.getInt(KEY_LAST_SHOWN_OFFER_DAY, 0)
        set(value) {
            sharedPreferences.edit().putInt(KEY_LAST_SHOWN_OFFER_DAY, value).apply()
        }

    var firstLaunchDate: Long
        get() = sharedPreferences.getLong(KEY_FIRST_LAUNCH_DATE, 0)
        set(value) {
            sharedPreferences.edit().putLong(KEY_FIRST_LAUNCH_DATE, value).apply()
        }

    var isAppOpenAllowedFromRemoteConfig
        get() = sharedPreferences.getBoolean(IS_APP_OPEN_ALLOWED_FROM_REMOTE_CONFIG, true)
        set(value) = sharedPreferences.edit { putBoolean(IS_APP_OPEN_ALLOWED_FROM_REMOTE_CONFIG, value) }

    var isSplashInterstitialAllowedFromRemoteConfig
        get() = sharedPreferences.getBoolean(IS_SPLASH_INTERSTITIAL_ALLOWED_FROM_REMOTE_CONFIG, true)
        set(value) = sharedPreferences.edit { putBoolean(IS_SPLASH_INTERSTITIAL_ALLOWED_FROM_REMOTE_CONFIG, value) }

    var sessionCount: Int
        get() = sharedPreferences.getInt(SESSION_COUNT, 0)
        set(value) = sharedPreferences.edit { putInt(SESSION_COUNT, value) }

    var isSplashPremiumFirstVisit
        get() = sharedPreferences.getBoolean(IS_SPLASH_PREMIUM_FIRST_VISIT, true)
        set(value) = sharedPreferences.edit { putBoolean(IS_SPLASH_PREMIUM_FIRST_VISIT, value) }

    var isOnboardingShown
        get() = sharedPreferences.getBoolean(IS_ONBOARDING_SHOWN, false)
        set(value) = sharedPreferences.edit { putBoolean(IS_ONBOARDING_SHOWN, value)}

    var isLanguagesShown
        get() = sharedPreferences.getBoolean(IS_LANGUAGES_SHOWN, false)
        set(value) = sharedPreferences.edit { putBoolean(IS_LANGUAGES_SHOWN, value) }

    var isBrowserGuideShown
        get() = sharedPreferences.getBoolean(IS_BROWSER_GUIDE_SHOWN, false)
        set(value) = sharedPreferences.edit { putBoolean(IS_BROWSER_GUIDE_SHOWN, value) }

    var browserDownload
        get() = sharedPreferences.getString(BROWSER_DOWNLOAD, "")
        set(value) = sharedPreferences.edit { putString(BROWSER_DOWNLOAD, value) }

    var selectedServiceMode
        get() = sharedPreferences.getString(SELECTED_SERVICE_MODE, Key.modeVpn)
        set(value) = sharedPreferences.edit { putString(SELECTED_SERVICE_MODE, value) }

    var lastPremiumShownTime: Long
        get() = sharedPreferences.getLong(LAST_PREMIUM_SHOWN_TIME, 0)
        set(value) = sharedPreferences.edit { putLong(LAST_PREMIUM_SHOWN_TIME, value) }

    var lastRewardTime: Long
        get() = sharedPreferences.getLong(LAST_REWARD_TIME, 0)
        set(value) = sharedPreferences.edit { putLong(LAST_REWARD_TIME, value) }

    var freeVpnTime
        get() = sharedPreferences.getLong(FREE_VPN_TIME, FREE_VPN_TIME_VALUE)
        set(value) = sharedPreferences.edit { putLong(FREE_VPN_TIME, value) }

    var isAdsRemoved
        get() = sharedPreferences.getBoolean(IS_PREMIUM, false)
        set(value) = sharedPreferences.edit { putBoolean(IS_PREMIUM, value) }

    var hasTermsAndConditionsAccepted
        get() = sharedPreferences.getBoolean(HAS_TERM_AND_CONDITION_ACCEPTED, false)
        set(value) = sharedPreferences.edit { putBoolean(HAS_TERM_AND_CONDITION_ACCEPTED, value) }

    var shouldShowReport
        get() = sharedPreferences.getBoolean(SHOULD_SHOW_REPORT, false)
        set(value) = sharedPreferences.edit { putBoolean(SHOULD_SHOW_REPORT, value) }

    var selectedLanguage
        get() = getObject(SELECTED_LANG, LanguageModel::class.java)
        set(value) = sharedPreferences.edit { putObject(SELECTED_LANG, value) }

    var lastSelectedProfile
        get() = getObject(LAST_SELECTED_SERVER, ServerModel::class.java)
        set(value) = sharedPreferences.edit { putObject(LAST_SELECTED_SERVER, value) }

    var selectedProfile
        get() = getObject(SELECTED_SERVER, ServerModel::class.java)
        set(value) = sharedPreferences.edit { putObject(SELECTED_SERVER, value) }

    var selectedProxyProfile
        get() = getObject(SELECTED_SERVER_PROXY, ServerModel::class.java)
        set(value) = sharedPreferences.edit { putObject(SELECTED_SERVER_PROXY, value) }

    fun isAppAllowed(appName: String): Boolean {
        val apps = disabledApps()
        return apps?.contains(appName)!!
    }

    fun addDisableApp(appName: String) {
        val apps = disabledApps()
        apps?.add(appName)
        updateStorage(apps?.toMutableSet()!!)
    }

    fun removeDisableApp(appName: String) {
        val apps = disabledApps()
        if (apps?.contains(appName) == true)
            apps.remove(appName)
        updateStorage(apps?.toMutableSet()!!)
    }

    fun disabledApps(): HashSet<String>? {
        return sharedPreferences.getStringSet(DISABLED_APPS, mutableSetOf<String>())?.toHashSet()
    }

    private fun updateStorage(appSet: MutableSet<String>) {
        sharedPreferences.edit { putStringSet(DISABLED_APPS, appSet) }
    }

    private fun <T> getObject(key: String, classType: Class<T>): T? {
        return when (val str = sharedPreferences.getString(key, null)) {
            null -> null
            else -> Gson().fromJson(str, classType)
        }
    }

    private fun <T> putObject(key: String, value: T) {
        val editor = sharedPreferences.edit()
        editor.putString(key, Gson().toJson(value))
        editor.apply()
    }
}