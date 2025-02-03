package ai.galixo.proxy.admob

import ai.galixo.proxy.utils.extensions.isOnline
import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

// Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
private var isMobileAdsInitializeCalled = AtomicBoolean(false)

fun Activity.initializeMobileAdsSdk() {
    if (isMobileAdsInitializeCalled.getAndSet(true)) {
        return
    }

    val backgroundScope = CoroutineScope(Dispatchers.IO)
    backgroundScope.launch {
        // Initialize the Google Mobile Ads SDK on a background thread.
        MobileAds.initialize(this@initializeMobileAdsSdk)
    }
}

fun Activity.requestConsentForm(isConsentGathered: (Boolean) -> Unit) {

    if (!isOnline()) {
        isConsentGathered(false)
        return
    }
    // For testing purpose only, remove it from production code
    val debugSettings = ConsentDebugSettings.Builder(this)
        .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
        .addTestDeviceHashedId("6FEB41E24631374607F855DE23AB787B")
        .build()

    // Set tag for under age of consent. false means users are not under age
    // of consent.
    val params = ConsentRequestParameters
        .Builder()
//        .setConsentDebugSettings(debugSettings) // For testing purpose only, remove it from production code
        .setTagForUnderAgeOfConsent(false)
        .build()


    val consentInformation: ConsentInformation = UserMessagingPlatform.getConsentInformation(this)
//    consentInformation.reset() // For testing purpose only, remove it from production code

    if (consentInformation.canRequestAds()) {
        initializeMobileAdsSdk()
        isConsentGathered(true)

    } else {
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this
                ) { loadAndShowError ->
                    // Consent gathering failed.
                    Timber.w(
                        String.format(
                            "%s: %s",
                            loadAndShowError?.errorCode,
                            loadAndShowError?.message
                        )
                    )

                    // Consent has been gathered.
                    if (consentInformation.canRequestAds()) {
                        initializeMobileAdsSdk()
                        isConsentGathered(true)
                    }
                }
            },
            { requestConsentError ->
                // Consent gathering failed.
                Timber.w(
                    String.format(
                        "%s: %s",
                        requestConsentError.errorCode,
                        requestConsentError.message
                    )
                )
                isConsentGathered(false)
            })

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        /*if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk()
        }*/
    }
}

fun Context.loadAdmobInterstitial(
    adId: String,
    onAdLoaded: (InterstitialAd) -> Unit,
    onAdFailedToLoad: (LoadAdError) -> Unit
) {
    val adRequest = AdRequest.Builder().build()
    InterstitialAd.load(this, adId, adRequest, object : InterstitialAdLoadCallback() {
        var mInterstitialAd: InterstitialAd? = null

        override fun onAdFailedToLoad(error: LoadAdError) {
            super.onAdFailedToLoad(error)
            mInterstitialAd = null
            onAdFailedToLoad(error)

        }

        override fun onAdLoaded(ad: InterstitialAd) {
            super.onAdLoaded(ad)
            mInterstitialAd = ad
            mInterstitialAd?.let {
                onAdLoaded(it)
            }
        }
    })
}

fun Activity.loadAndShowRewardedAd(
    adUnitId: String,
    onAdFailedToLoad: (LoadAdError) -> Unit,
    onAdLoaded: (ad: RewardedAd) -> Unit
) {
    val adRequest = AdRequest.Builder().build()

    RewardedAd.load(this, adUnitId, adRequest, object : RewardedAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            onAdFailedToLoad(adError)
        }

        override fun onAdLoaded(ad: RewardedAd) {
            onAdLoaded(ad)
        }
    })
}

fun Activity.loadExtraTimeInterstitial(
    onAdLoaded: (InterstitialAd) -> Unit,
    onAdFailedToLoad: (LoadAdError) -> Unit
) {
    val adRequest = AdRequest.Builder().build()
    InterstitialAd.load(this,
        admob_interstitial_id_timer,
        adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                onAdFailedToLoad(adError)
            }

            override fun onAdLoaded(ad: InterstitialAd) {
                onAdLoaded(ad)
            }
        })
}