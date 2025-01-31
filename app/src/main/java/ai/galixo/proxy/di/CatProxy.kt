package ai.galixo.proxy.di

import ai.galixo.cypherlib.NativeLib
import ai.galixo.proxy.BuildConfig
import ai.galixo.proxy.presentation.iap.BillingViewModel
import ai.galixo.proxy.presentation.main.MainActivity
import android.app.Application
import android.content.res.Configuration
import com.github.shadowsocks.Core
import com.github.shadowsocks.preference.PrefUtils
import com.github.shadowsocks.preference.PrefUtils.isAdsRemoved
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class CatProxy: Application() {

    @Inject
    lateinit var billingViewModel: BillingViewModel

    override fun onCreate() {
        super.onCreate()

        Core.init(this, MainActivity::class)
        PrefUtils.init(this)
        NativeLib.loadLibrary()

        billingViewModel.startBillingConnection()

        CoroutineScope(Dispatchers.IO).launch {
            billingViewModel.purchases.collect { purchaseList ->
                Timber.d("purchase list: $purchaseList")
                isAdsRemoved = purchaseList?.isNotEmpty() ?: false
            }
        }

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