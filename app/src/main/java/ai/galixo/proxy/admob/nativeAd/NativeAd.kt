package ai.galixo.proxy.admob.nativeAd

import android.app.Activity
import com.github.shadowsocks.preference.PrefUtils
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT
import timber.log.Timber

object NativeAd {
    var admobNativeAd: NativeAd? = null
    var isLoading = false
    var result: ((Boolean) -> Unit)? = null

    fun loadAd(activity: Activity, adId: String) {
        if (PrefUtils.isAdsRemoved) {
            result?.invoke(false)
            return
        }

        if (adId.isEmpty()) {
            result?.invoke(false)
            return
        }

        if (admobNativeAd != null) {
            result?.invoke(true)
            return
        }

        if (isLoading) {
            return
        }

        Timber.d("connected native called!")
        isLoading = true
        val builder = AdLoader.Builder(activity, adId)
        builder.forNativeAd { ad ->
            admobNativeAd = ad
        }

        val videoOptions =
            VideoOptions.Builder().setStartMuted(true).build()

        val adOptions =
            NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(
                ADCHOICES_TOP_RIGHT
            ).build()

        builder.withNativeAdOptions(adOptions)

        val adLoader =
            builder
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        Timber.d("connected native onAdFailedToLoad!")
                        admobNativeAd = null
                        isLoading = false
                        result?.invoke(false)
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        Timber.d("connected native onAdLoaded!")
                        isLoading = false
                        result?.invoke(true)
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
//                        admobNativeAd = null
                    }
                }
                )
                .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }
}