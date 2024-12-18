package com.galixo.proxy.compose.admob

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adId: String = "ca-app-pub-3940256099942544/6300978111",
    onAdLoaded: () -> Unit,
    onFailedToLoad: () -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val adaptiveAds = AdaptiveAds(context)
            val adView = AdView(context)
            adView.apply {
                setAdSize(adaptiveAds.adSize)
                adUnitId = adId
                loadAd(AdRequest.Builder().build())
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        onAdLoaded()
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        onFailedToLoad()
                    }
                }
            }
        },
        update = { adView ->
            adView.loadAd(AdRequest.Builder().build())
        }
    )
}
