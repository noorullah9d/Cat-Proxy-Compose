package ai.galixo.proxy.presentation.dialogs

import ai.galixo.proxy.R
import ai.galixo.proxy.presentation.components.LottieAnimation
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.shadowsocks.preference.PrefUtils

@Composable
fun DisconnectVpnDialog(
    onDismiss: () -> Unit,
    onDisconnectClick: () -> Unit
) {
//    val isAdsRemoved = PrefUtils.isAdsRemoved
    var isAnimationFinished by remember { mutableStateOf(/*isAdsRemoved*/false) } // If ads are removed, show button immediately

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            shape = CatTheme.shape.dialog,
            color = CatTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Subtitle
                Text(
                    text = stringResource(id = R.string.disconnect_vpn_summary),
                    style = CatTheme.typography.semiBold.copy(
                        fontSize = 18.sp
                    ),
                    color = CatTheme.colorScheme.textPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons Row (Cancel Left, Disconnect/Animation Right)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Cancel Button (Left)
                    Button(
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, CatTheme.colorScheme.divider),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CatTheme.colorScheme.background
                        ),
                        onClick = { onDismiss() }
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp),
                            text = stringResource(id = R.string.cancel),
                            color = CatTheme.colorScheme.textTertiary,
                            style = CatTheme.typography.semiBold.copy(
                                fontSize = 14.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Animation OR Disconnect Button (Right)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!isAnimationFinished) {
                            LottieAnimation(
                                modifier = Modifier.height(45.dp).fillMaxWidth(),
                                animationRes = R.raw.anim_disconnect_button,
                                onAnimationEnd = {
                                    isAnimationFinished = true
                                } // Hide animation when finished
                            )
                        }

                        if (isAnimationFinished) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CatTheme.colorScheme.errorSurface
                                ),
                                onClick = {
                                    onDisconnectClick()
                                    onDismiss()
                                }
                            ) {
                                Text(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    text = stringResource(id = R.string.disconnect),
                                    color = CatTheme.colorScheme.onErrorSurface,
                                    style = CatTheme.typography.semiBold.copy(
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewDisconnectVpnDialog() {
    CatTheme {
        DisconnectVpnDialog(
            onDismiss = {},
            onDisconnectClick = {}
        )
    }
}