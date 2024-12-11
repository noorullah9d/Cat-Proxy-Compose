package com.galixo.proxy.compose.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.galixo.proxy.compose.domain.model.LanguageModel
import com.galixo.proxy.compose.domain.model.ServerModel
import com.galixo.proxy.compose.utils.Constants.APP_PREFS
import com.galixo.proxy.compose.utils.Constants.LAST_SELECTED_SERVER
import com.galixo.proxy.compose.utils.Constants.SELECTED_LANG
import com.galixo.proxy.compose.utils.Constants.SELECTED_SERVER

object PrefUtils {
    private lateinit var sharedPreferences: SharedPreferences
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
    }

    var lastSelectedProfile
        get() = getObject(LAST_SELECTED_SERVER, ServerModel::class.java)
        set(value) = sharedPreferences.edit { putObject(LAST_SELECTED_SERVER, value) }

    var selectedProfile
        get() = getObject(SELECTED_SERVER, ServerModel::class.java)
        set(value) = sharedPreferences.edit { putObject(SELECTED_SERVER, value) }

    var selectedLanguage
        get() = getObject(SELECTED_LANG, LanguageModel::class.java)
        set(value) = sharedPreferences.edit { putObject(SELECTED_LANG, value) }

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