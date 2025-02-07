package ai.galixo.proxy.admob.nativeAd

import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.ui.theme.orange
import android.graphics.drawable.Drawable
import android.view.View
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import timber.log.Timber

@Composable
fun CallNativeAd(
    nativeAd: NativeAd,
    adSize: NativeAdSize
) {
    NativeAdView(ad = nativeAd) { ad, view ->
        when(adSize) {
            NativeAdSize.SMALL -> { PopulateSmallNative(ad, view) }
            NativeAdSize.LARGE -> { NativeAdXmlComposable(ad, view) }
        }
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
private fun PopulateSmallNative(nativeAd: NativeAd?, composeView: View?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .combinedClickable {
                composeView?.performClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = CatTheme.colorScheme.adBackground
        )
    ) {
        nativeAd?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    val icon: Drawable? = it.icon?.drawable
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
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            maxLines = 1,
                            text = it.headline ?: "Headline",
                            style = CatTheme.typography.itemTitle,
                            color = CatTheme.colorScheme.textPrimary,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text(
                                text = "Ad",
                                style = MaterialTheme.typography.labelSmall,
                                color = CatTheme.colorScheme.textSecondary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp)) // Increased corner radius
                                    .border(
                                        width = 1.dp,
                                        color = CatTheme.colorScheme.textSecondary, // Stroke color
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            /*StarRatingBar(
                                rating = it.starRating!!.toFloat(),
                                onRatingChanged = {}
                            )*/
                            Text(
                                maxLines = 1,
                                modifier = Modifier.fillMaxWidth(),
                                text = it.body ?: "Body text goes here...",
                                style = MaterialTheme.typography.bodySmall,
                                color = CatTheme.colorScheme.textSecondary,
                            )
                        }
                    }

                    it.callToAction?.let { cta ->
                        Button(
                            modifier = Modifier
                                .width(120.dp)
                                .clip(shape = RoundedCornerShape(50.dp)),
                            onClick = {
                                composeView?.performClick()
                            },
                            border = BorderStroke(1.dp, orange),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CatTheme.colorScheme.adBackground
                            ),
                            content = {
                                Text(
                                    text = cta.uppercase(),
                                    color = CatTheme.colorScheme.textPrimary,
                                    style = CatTheme.typography.semiBold.copy(
                                        fontSize = 12.sp
                                    ),
                                    maxLines = 1
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PopulateLargeNative(nativeAd: NativeAd?, composeView: View?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(CardDefaults.shape)
            .combinedClickable {
                composeView?.performClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = CatTheme.colorScheme.adBackground
        )
    ) {
        nativeAd?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    val icon: Drawable? = it.icon?.drawable
                    icon?.let { drawable ->
                        Image(
                            painter = rememberAsyncImagePainter(model = drawable),
                            contentDescription = "Ad",
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
                            text = it.headline ?: "Headline",
                            style = CatTheme.typography.itemTitle,
                            color = CatTheme.colorScheme.textPrimary,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text(
                                text = "Ad",
                                style = MaterialTheme.typography.labelSmall,
                                color = CatTheme.colorScheme.textSecondary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp)) // Increased corner radius
                                    .border(
                                        width = 1.dp,
                                        color = CatTheme.colorScheme.textSecondary, // Stroke color
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            /*StarRatingBar(
                                rating = it.starRating!!.toFloat(),
                                onRatingChanged = {}
                            )*/
                            Text(
                                maxLines = 1,
                                modifier = Modifier.fillMaxWidth(),
                                text = it.body ?: "Body text goes here...",
                                style = MaterialTheme.typography.bodySmall,
                                color = CatTheme.colorScheme.textSecondary,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Timber.d("CallNativeAd: media view= ${it.mediaContent}, hasVideo=${it.mediaContent?.hasVideoContent()}")

                if (it.mediaContent != null) {
                    AndroidView(
                        factory = { context ->
                            MediaView(context).apply {
                                it.mediaContent?.let { mediaContent ->
                                    setMediaContent(mediaContent)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp) // Adjust height as needed
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                it.callToAction?.let { cta ->
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(50.dp)),
                        onClick = {
                            composeView?.performClick()
                        },
                        border = BorderStroke(1.dp, orange),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CatTheme.colorScheme.adBackground
                        ),
                        content = {
                            Text(
                                text = cta.uppercase(),
                                color = CatTheme.colorScheme.textPrimary,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    )
                }
            }
        }
    }
}