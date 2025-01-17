package ai.galixo.proxy.di

import ai.galixo.cypherlib.NativeLib
import ai.galixo.proxy.BuildConfig
import ai.galixo.proxy.MainActivity
import android.app.Application
import android.content.res.Configuration
import com.github.shadowsocks.Core
import com.github.shadowsocks.preference.PrefUtils
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CatProxy: Application() {
    override fun onCreate() {
        super.onCreate()

        Core.init(this, MainActivity::class)
        PrefUtils.init(this)
        NativeLib.loadLibrary()

        if (BuildConfig.DEBUG) {
            if (Timber.treeCount == 0) {
                Timber.plant(Timber.DebugTree())
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Core.updateNotificationChannels()
    }
}