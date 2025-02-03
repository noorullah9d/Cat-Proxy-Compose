package ai.galixo.proxy.admob.nativeAd

import ai.galixo.proxy.R
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.material.imageview.ShapeableImageView

@Composable
fun NativeAdXmlComposable(nativeAd: NativeAd?, composeView: View?) {
    AndroidView(
        factory = { context ->
            // Inflate your XML layout that wraps the NativeAdView
            LayoutInflater.from(context)
                .inflate(R.layout.gnt_languages, null) as NativeAdView
        },
        update = { adView ->
            nativeAd?.let { ad ->
                // Bind the ad asset views using the IDs from your XML
                adView.headlineView = adView.findViewById(R.id.primary)
                adView.mediaView = adView.findViewById(R.id.ad_media)
                adView.bodyView = adView.findViewById(R.id.body)
                adView.iconView = adView.findViewById(R.id.icon)
                adView.callToActionView = adView.findViewById(R.id.cta)

                // Populate the views with ad data
                (adView.iconView as? ShapeableImageView)?.setImageDrawable(ad.icon?.drawable)
                (adView.headlineView as? TextView)?.text = ad.headline
                (adView.bodyView as? TextView)?.text = ad.body
                (adView.callToActionView as? TextView)?.text = ad.callToAction

                // Set media content if available
                ad.mediaContent?.let { mediaContent ->
                    adView.mediaView?.mediaContent = mediaContent
                }

                // Register the native ad object with the NativeAdView.
                // This is required by the Ad SDK to handle impression and click tracking.
                adView.setNativeAd(ad)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}
