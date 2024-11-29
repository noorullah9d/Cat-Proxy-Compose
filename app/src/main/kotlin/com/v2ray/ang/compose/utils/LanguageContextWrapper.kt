package com.v2ray.ang.compose.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.LocaleList
import java.util.Locale

class LanguageContextWrapper(base: Context?) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context?, newLocale: Locale?): LanguageContextWrapper {
            if (context == null) return LanguageContextWrapper(context)
            val configuration = context.resources.configuration
            configuration.setLocale(newLocale)
            val localeList = LocaleList(newLocale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            return LanguageContextWrapper(context.createConfigurationContext(configuration))
        }
    }
}