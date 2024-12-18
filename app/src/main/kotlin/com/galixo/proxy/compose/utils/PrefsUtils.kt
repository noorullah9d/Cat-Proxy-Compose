package com.galixo.proxy.compose.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.galixo.proxy.compose.domain.model.LanguageModel
import com.galixo.proxy.compose.domain.model.ServerModel
import com.galixo.proxy.compose.utils.Constants.APP_PREFS
import com.galixo.proxy.compose.utils.Constants.DISABLED_APPS
import com.galixo.proxy.compose.utils.Constants.HAS_TERM_AND_CONDITION_ACCEPTED
import com.galixo.proxy.compose.utils.Constants.LAST_SELECTED_SERVER
import com.galixo.proxy.compose.utils.Constants.SELECTED_LANG
import com.galixo.proxy.compose.utils.Constants.SELECTED_SERVER

object PrefUtils {
    private lateinit var sharedPreferences: SharedPreferences
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
    }

    var hasTermsAndConditionsAccepted
        get() = sharedPreferences.getBoolean(HAS_TERM_AND_CONDITION_ACCEPTED, false)
        set(value) = sharedPreferences.edit { putBoolean(HAS_TERM_AND_CONDITION_ACCEPTED, value) }

    var lastSelectedProfile
        get() = getObject(LAST_SELECTED_SERVER, ServerModel::class.java)
        set(value) = sharedPreferences.edit { putObject(LAST_SELECTED_SERVER, value) }

    var selectedProfile
        get() = getObject(SELECTED_SERVER, ServerModel::class.java)
        set(value) = sharedPreferences.edit { putObject(SELECTED_SERVER, value) }

    var selectedLanguage
        get() = getObject(SELECTED_LANG, LanguageModel::class.java)
        set(value) = sharedPreferences.edit { putObject(SELECTED_LANG, value) }

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