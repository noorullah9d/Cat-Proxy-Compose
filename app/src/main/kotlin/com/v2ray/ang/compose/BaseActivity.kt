package com.v2ray.ang.compose

import android.content.Context
import androidx.activity.ComponentActivity
import com.v2ray.ang.compose.utils.LanguageContextWrapper
import com.v2ray.ang.compose.utils.PrefUtils.selectedLanguage
import java.util.Locale

abstract class BaseActivity: ComponentActivity() {

    override fun attachBaseContext(context: Context?) {
        val languageCode = selectedLanguage?.languageCode ?: "en"
        val locale = Locale.forLanguageTag(languageCode)
        super.attachBaseContext(LanguageContextWrapper.wrap(context, locale))
    }
}