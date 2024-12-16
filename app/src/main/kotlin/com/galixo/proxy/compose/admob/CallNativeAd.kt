package com.galixo.proxy.compose.admob

import android.graphics.drawable.Drawable
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.galixo.proxy.compose.theme.CatProxyTheme
import com.galixo.proxy.compose.theme.color_light_ad_background
import com.galixo.proxy.compose.theme.color_light_btn_text
import com.galixo.proxy.compose.theme.color_light_primary
import com.galixo.proxy.compose.theme.color_light_text_primary
import com.galixo.proxy.compose.theme.color_light_text_secondary
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun CallNativeAd(nativeAd: NativeAd) {
    NativeAdView(ad = nativeAd) { ad, view ->
        LoadAdContent(ad, view)
    }
}

@Composable
fun NativeAdView(
    ad: NativeAd,
    adContent: @Composable (ad: NativeAd, contentView: View) -> Unit,
) {
    val contentViewId by remember { mutableIntStateOf(View.generateViewId()) }
    val adViewId by remember { mutableIntStateOf(View.generateViewId()) }
    AndroidView(
        factory = { context ->
            val contentView = ComposeView(context).apply {
                id = contentViewId
            }
            NativeAdView(context).apply {
                id = adViewId
                addView(contentView)
            }
        },
        update = { view ->
            val adView = view.findViewById<NativeAdView>(adViewId)
            val contentView = view.findViewById<ComposeView>(contentViewId)

            adView.setNativeAd(ad)
            adView.callToActionView = contentView
            contentView.setContent { adContent(ad, contentView) }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LoadAdContent(nativeAd: NativeAd?, composeView: View?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(CardDefaults.shape)
            .combinedClickable {
                composeView?.performClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = color_light_ad_background
        )
    ) {
        nativeAd.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    val icon: Drawable? = it?.icon?.drawable
                    icon?.let { drawable ->
                        Image(
                            painter = rememberAsyncImagePainter(model = drawable),
                            contentDescription = "Ad"/*it.icon?.contentDescription*/,
                            modifier = Modifier
                                .size(65.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column(
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = it?.headline ?: "Headline",
                            style = MaterialTheme.typography.bodyLarge,
                            color = color_light_text_primary,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row {
                            Text(
                                text = "Ad",
                                style = MaterialTheme.typography.labelSmall,
                                color = color_light_text_secondary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp)) // Increased corner radius
                                    .border(
                                        width = 1.dp,
                                        color = color_light_text_secondary, // Stroke color
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = it?.body ?: "Body text goes here...",
                    style = MaterialTheme.typography.bodySmall,
                    color = color_light_text_secondary,
                )

                Spacer(modifier = Modifier.height(4.dp))

                it?.callToAction?.let { cta ->
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            composeView?.performClick()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = color_light_primary
                        ),
                        content = {
                            Text(
                                text = cta.uppercase(),
                                color = color_light_btn_text,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun NativeAdPreview() {
    CatProxyTheme {
        LoadAdContent(
            nativeAd = null,
            composeView = null
        )
    }
}
