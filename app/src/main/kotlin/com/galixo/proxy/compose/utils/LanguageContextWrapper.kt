package com.galixo.proxy.compose.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import java.util.Locale

class LanguageContextWrapper(base: Context?) : ContextWrapper(base) {
    companion object {
        @RequiresApi(Build.VERSION_CODES.N)
        fun wrap(context: Context?, newLocale: Locale?): LanguageContextWrapper {
            var mContext = context
            if (context == null) return LanguageContextWrapper(context)
            val configuration = context.resources.configuration

            mContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocale(newLocale)
                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
                mContext.createConfigurationContext(configuration)
            } else {
                configuration.setLocale(newLocale)
                mContext.createConfigurationContext(configuration)
            }
            return LanguageContextWrapper(mContext)
        }
    }
}