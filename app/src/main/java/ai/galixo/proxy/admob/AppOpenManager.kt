package ai.galixo.proxy.admob

import ai.galixo.proxy.presentation.main.MainActivity
import ai.galixo.proxy.utils.extensions.isOnline
import ai.galixo.proxy.utils.isAnySystemDialogShown
import ai.galixo.proxy.utils.isShowingAd
import ai.galixo.proxy.utils.shouldShowInterstitialAd
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.github.shadowsocks.preference.PrefUtils
import com.google.android.gms.ads.AdActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import timber.log.Timber

class AppOpenManager(private var application: Application) :
    LifecycleObserver,
    Application.ActivityLifecycleCallbacks {


    var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAdLoadCallback? = null

    private var currentActivity: Activity? = null

    private var isLoadingAd = false
    var shouldShowAd = true /* used for stopping ad to appear forcefully */
    var isAppInBackground = true
    private var appInBackgroundTime: Long = 0
    private val backgroundThreshold: Long = 5000

    /** Creates and returns ad request.  */
    private val adRequest: AdRequest
        get() = AdRequest.Builder().build()

    /** Utility method that checks if ad exists and can be shown.  */
    private val isAdAvailable: Boolean get() = appOpenAd != null

    init {
        this.application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        shouldShowInterstitialAd = false
        isAnySystemDialogShown = false
    }

    fun destroyAd() {
        currentActivity = null
        appOpenAd = null
    }

    /** Request an ad  */
    fun fetchAd() {
        if (isAdAvailable ||
            isLoadingAd ||
            !application.isOnline()
        ) {
            return
        }

        if (PrefUtils.isAdsRemoved) {
            return
        }

        loadCallback = object : AppOpenAdLoadCallback() {

            override fun onAdLoaded(p0: AppOpenAd) {
                super.onAdLoaded(p0)
                appOpenAd = p0
                Timber.d("splash app open ad loaded")
                isLoadingAd = false
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                isLoadingAd = false
                Timber.d("splash app open ad failed${p0.message}")
            }
        }
        isLoadingAd = true
        AppOpenAd.load(
            application.applicationContext,
            admob_app_open_id,
            adRequest,
            loadCallback!!
        )
    }

    /** Shows the ad if one isn't already showing.  */
    fun showAdIfAvailable(
        onAdShown: () -> Unit,
        onDismissed: () -> Unit
    ) {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.

        if (PrefUtils.isAdsRemoved) {
            onDismissed.invoke()
            return
        }

        try {
            if (!isShowingAd && isAdAvailable) {
                val fullScreenContentCallback: FullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null
                            isShowingAd = false
                            onDismissed.invoke()
                            shouldShowInterstitialAd = true
                        }

                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            isShowingAd = true
                            onAdShown.invoke()
                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            super.onAdFailedToShowFullScreenContent(p0)
                            onDismissed.invoke()
                            appOpenAd = null
                            shouldShowInterstitialAd = true
                        }
                    }
                appOpenAd?.fullScreenContentCallback = fullScreenContentCallback

                if (currentActivity != null && shouldShowAd) {
                    shouldShowInterstitialAd = true
                    currentActivity?.let {
                        appOpenAd?.show(it)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** LifecycleObserver methods  */
    @OnLifecycleEvent(ON_START)
    fun onStart() {

        /*        try {
                    showAdIfAvailable()
                } catch (e: Exception) {
                    e.printStackTrace()
                }*/
    }


    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
//        showReport = true
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity

        if (!PrefUtils.isAdsRemoved) {
            if (!shouldPause(activity) || isAnySystemDialogShown /*|| isChooserShown*/) {
                isAppInBackground = false
                isAnySystemDialogShown = false
            }
            if (isAppInBackground && appInBackgroundTime != 0L) {
                // App is resuming from the background
                isAppInBackground = false

                val currentTime = System.currentTimeMillis()
                val timeInBackground = currentTime - appInBackgroundTime
                if (timeInBackground > backgroundThreshold) {
                    currentActivity?.apply {

                        if (this !is AdActivity) {
                            Timber.d("AppOpenManager4: starting StringActivity")
                            val intent = Intent(currentActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finishAffinity()
                            finish()
                            overridePendingTransition(0, 0)
                        }
                    }
                }
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (shouldPause(activity) && !PrefUtils.isAdsRemoved) {
            isAppInBackground = true
            appInBackgroundTime = System.currentTimeMillis()
        }
    }

    private fun shouldPause(activity: Activity): Boolean {
        return /*activity is SelectLanguageActivity || activity is PremiumActivity || activity is OnboardingActivity ||*/ activity is MainActivity
    }

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) {}
}