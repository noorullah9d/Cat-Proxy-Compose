package ai.galixo.proxy.presentation.base

import ai.galixo.proxy.utils.LanguageContextWrapper
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.github.shadowsocks.preference.PrefUtils.selectedLanguage
import java.util.Locale

abstract class BaseActivity: ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun attachBaseContext(context: Context?) {
        val languageCode = selectedLanguage?.languageCode ?: "en"
        val locale = Locale.forLanguageTag(languageCode)
        super.attachBaseContext(LanguageContextWrapper.Companion.wrap(context, locale))
    }
}