package ai.galixo.proxy.presentation.home

import ai.galixo.proxy.R
import ai.galixo.proxy.domain.model.Stats
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.presentation.components.LottieAnimation
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieConstants
import com.github.shadowsocks.preference.ServerModel
import timber.log.Timber

@Composable
fun ConnectedContent(
    modifier: Modifier,
    navController: NavController,
    selectedServer: ServerModel?,
    preConnectionState: MutableState<HomeUiState>?,
    disconnectVpn: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CatTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(250.dp)
                .weight(1f),
            animationRes = R.raw.anim_connected,
            iterations = LottieConstants.IterateForever
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier,
            text = stringResource(R.string.remaining_rewarded_time),
            style = CatTheme.typography.itemTitle,
            color = CatTheme.colorScheme.textTertiary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier,
            text = "00:18:03",
            style = CatTheme.typography.toolbarTitle,
            color = CatTheme.colorScheme.textPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = {
                when (preConnectionState?.value) {
                    is HomeUiState.Connected -> {
                        disconnectVpn()
                    }

                    else -> {
                        Timber.d("HomeScreen: unknown")
                    }
                }
            },
            modifier = Modifier
                .padding(bottom = 10.dp),
            border = BorderStroke(1.dp, CatTheme.colorScheme.divider),
            shape = CatTheme.shape.button,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = CatTheme.colorScheme.background
            )
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                text = stringResource(R.string.disconnect),
                style = CatTheme.typography.itemTitle,
                color = CatTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ConnectedServer(
            server = selectedServer ?: ServerModel(
                countryName = "Unknown",
                cityName = "",
                flag = ""
            ),
            trafficStats = Stats(),
            onClick = {
                navController.navigate(Destinations.ServersScreen.route)
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .background(
                    color = CatTheme.colorScheme.adBackground,
                    shape = CatTheme.shape.ad
                )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectedContentPreview() {

    val dummyServer = ServerModel(
        countryName = "Unknown",
        cityName = "",
        flag = ""
    )
    CatTheme {
        ConnectedContent(
            modifier = Modifier,
            navController = rememberNavController(),
            selectedServer = dummyServer,
            preConnectionState = null,
            disconnectVpn = {}
        )
    }
}
