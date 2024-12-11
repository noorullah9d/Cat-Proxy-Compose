package com.galixo.proxy.compose

import android.content.Context
import androidx.activity.ComponentActivity
import com.galixo.proxy.compose.utils.LanguageContextWrapper
import com.galixo.proxy.compose.utils.PrefUtils.selectedLanguage
import java.util.Locale

abstract class BaseActivity: ComponentActivity() {

    override fun attachBaseContext(context: Context?) {
        val languageCode = selectedLanguage?.languageCode ?: "en"
        val locale = Locale.forLanguageTag(languageCode)
        super.attachBaseContext(LanguageContextWrapper.wrap(context, locale))
    }
}