package com.galixo.proxy.compose.admob

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.galixo.proxy.compose.theme.color_light_shimmer
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
    } ?: Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color_light_shimmer),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Ad is loading...")
    }
}
