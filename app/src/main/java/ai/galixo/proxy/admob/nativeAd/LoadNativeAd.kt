package ai.galixo.proxy.admob.nativeAd

import android.content.Context
import androidx.compose.runtime.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT

@Composable
fun LoadNativeAd(
    adUnitId: String = "",
    context: Context,
    adSize: NativeAdSize = NativeAdSize.SMALL
) {
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var isAdFailed by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val adBuilder = AdLoader.Builder(context, adUnitId)

        adBuilder.forNativeAd { ad ->
            nativeAd = ad
            isAdFailed = false
        }

        val videoOptions =
            VideoOptions.Builder().setStartMuted(true).build()

        val adOptions =
            NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(
                ADCHOICES_TOP_RIGHT
            ).build()

        adBuilder.withNativeAdOptions(adOptions)

        val adLoader = adBuilder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                nativeAd = null
                isAdFailed = true
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())

        onDispose {
            nativeAd?.destroy()
        }
    }

    // Display the ad content when available
    if (!isAdFailed) {
        when (adSize) {
            NativeAdSize.SMALL -> {
                nativeAd?.let { ad ->
                    CallNativeAd(ad, NativeAdSize.SMALL)
                } ?: SmallNativeShimmer()
            }
            NativeAdSize.LARGE -> {
                nativeAd?.let { ad ->
                    CallNativeAd(ad, NativeAdSize.LARGE)
                } ?: LargeNativeShimmer()
            }
        }
    }
}
