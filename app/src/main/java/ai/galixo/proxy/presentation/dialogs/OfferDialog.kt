package ai.galixo.proxy.presentation.dialogs

import ai.galixo.proxy.R
import ai.galixo.proxy.presentation.components.LottieAnimation
import ai.galixo.proxy.presentation.components.OfferItem
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieConstants

@Composable
fun OfferDialog(
    onDismiss: () -> Unit,
    onOfferSelected: (Int) -> Unit,
    onPremiumClicked: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false) // Full screen
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CatTheme.colorScheme.background)
        ) {
            // Background Lottie Animation
            LottieAnimation(
                modifier = Modifier.fillMaxSize(),
                animationRes = R.raw.reward_lines,
                iterations = LottieConstants.IterateForever,
                onAnimationEnd = {}
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Cancel Button (Top Right)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    IconButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = { onDismiss() }
                    ) {
                        Icon(
                            modifier = Modifier.size(34.dp),
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Close",
                            tint = CatTheme.colorScheme.onBackground
                        )
                    }
                }

                // Reward Image
                Image(
                    painter = painterResource(id = R.drawable.img_cup),
                    contentDescription = "Cup",
                    modifier = Modifier.size(150.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Title
                Text(
                    text = stringResource(id = R.string.earn_free_vpn_time),
                    style = CatTheme.typography.extraBold.copy(
                        fontSize = 24.sp
                    ),
                    color = CatTheme.colorScheme.textPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = stringResource(id = R.string.reward_description),
                    style = CatTheme.typography.semiBold,
                    color = CatTheme.colorScheme.textTertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Offers List
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OfferItem(
                        iconRes = R.drawable.img_30min,
                        title = stringResource(id = R.string._30_minutes),
                        description = stringResource(id = R.string.offer_1_description),
                        onClick = { onOfferSelected(1) }
                    )
                    OfferItem(
                        iconRes = R.drawable.img_2hrs,
                        title = stringResource(id = R.string._2_hours),
                        description = stringResource(id = R.string.offer_2_description),
                        onClick = { onOfferSelected(2) }
                    )
                    OfferItem(
                        iconRes = R.drawable.img_24hrs,
                        title = stringResource(id = R.string._24_hours),
                        description = stringResource(id = R.string.offer_3_description),
                        onClick = { onOfferSelected(3) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Divider with "or" Text
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp),
                        color = CatTheme.colorScheme.divider
                    )

                    Text(
                        text = stringResource(id = R.string.or),
                        style = CatTheme.typography.itemTitle,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp),
                        color = CatTheme.colorScheme.divider
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Premium Button
                Image(
                    painter = painterResource(id = R.drawable.img_premium_blue),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = true) { onPremiumClicked() },
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OfferDialogPreview() {
    CatTheme {
        OfferDialog(
            {}, {}, {}
        )
    }
}

