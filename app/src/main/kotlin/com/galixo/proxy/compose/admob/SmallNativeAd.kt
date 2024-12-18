package com.galixo.proxy.compose.admob

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT

@Composable
fun SmallNativeAd(
    modifier: Modifier = Modifier,
    adUnitId: String = "",
    context: Context
) {
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {
        val adBuilder = AdLoader.Builder(context, adUnitId)

        adBuilder.forNativeAd { ad ->
            nativeAd = ad
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
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())

        onDispose {
            nativeAd?.destroy()
        }
    }

    // Display the ad content when available
    nativeAd?.let { ad ->
        CallNativeAd(ad)
    } ?: SmallNativeShimmer(modifier)
}
