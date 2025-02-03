package ai.galixo.proxy.presentation.home

import ai.galixo.proxy.R
import ai.galixo.proxy.admob.admob_native_id_home
import ai.galixo.proxy.admob.nativeAd.LoadNativeAd
import ai.galixo.proxy.admob.nativeAd.NativeAdSize
import ai.galixo.proxy.domain.model.Stats
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.presentation.base.UiState
import ai.galixo.proxy.presentation.components.LottieAnimation
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.PremiumSource
import ai.galixo.proxy.utils.extensions.toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieConstants
import com.github.shadowsocks.preference.ServerModel
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val selectedServer by viewModel.selectedServer.collectAsState()
    val homeUiState = remember { mutableStateOf<UiState<HomeUiState>>(UiState()) }
    val preConnectionState = remember { mutableStateOf<HomeUiState>(HomeUiState.Disconnected) }
    val vpnTrafficStats by viewModel.vpnTrafficStatsFlow.collectAsState()
    val shouldReconnect by viewModel.shouldReconnect.collectAsState()
    val context = LocalContext.current
    val progressToast = stringResource(R.string.connection_in_progress)

    Timber.d("HomeScreen: shouldReconnect= $shouldReconnect")

    LaunchedEffect(Unit) {
        viewModel.getPreConnectionStatus { state ->
            preConnectionState.value = state
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiState.collectLatest { state ->
            homeUiState.value = state
        }
    }

    // Handle reconnect action
    LaunchedEffect(shouldReconnect) {
        if (shouldReconnect) {
            viewModel.shouldReconnect(false)
            viewModel.reconnect()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar( // Dummy TopAppBar to occupy space
                title = { Spacer(modifier = Modifier) }
            )
        },
        content = { paddingValues ->
            HomeContent(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                selectedServer = selectedServer,
                homeUiState = homeUiState,
                trafficStats = vpnTrafficStats,
                onServerClick = {
                    when (homeUiState.value.data) {
                        is HomeUiState.Connecting -> {
                            context.toast(progressToast)
                        }
                        else -> {
                            navController.navigate(Destinations.ServersScreen.route)
                        }
                    }
                },
                connectVpn = {
                    if (viewModel.isServerSelected()) viewModel.connect()
                    else Timber.d("HomeScreen: server not selected!")
                },
                disconnectVpn = {
                    viewModel.stopConnection()
                }
            )
        }
    )
}

@Composable
fun HomeContent(
    modifier: Modifier,
    navController: NavController,
    selectedServer: ServerModel?,
    homeUiState: MutableState<UiState<HomeUiState>>?,
    trafficStats: Stats,
    onServerClick: () -> Unit,
    connectVpn: () -> Unit,
    disconnectVpn: () -> Unit
) {
    val context = LocalContext.current
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
            animationRes = when (homeUiState?.value?.data) {
                is HomeUiState.Connecting -> {
                    R.raw.anim_connecting
                }

                is HomeUiState.Connected -> {
                    R.raw.anim_connected
                }

                else -> {
                    R.raw.anim_not_connected
                }
            },
            iterations = LottieConstants.IterateForever
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (homeUiState?.value?.data) {
            is HomeUiState.Connecting -> {
                Text(
                    modifier = Modifier,
                    text = stringResource(R.string.establishing_secure_tunnel),
                    style = CatTheme.typography.itemTitle,
                    color = CatTheme.colorScheme.textSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                ConnectingButton()
            }

            is HomeUiState.Connected -> {
                Text(
                    modifier = Modifier,
                    text = stringResource(R.string.remaining_rewarded_time),
                    style = CatTheme.typography.itemTitle,
                    color = CatTheme.colorScheme.textTertiary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier,
                    text = trafficStats.connectionTime,
                    style = CatTheme.typography.toolbarTitle,
                    color = CatTheme.colorScheme.textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                DisconnectButton { disconnectVpn() }
            }

            else -> {
                ConnectButton(modifier) {
                    when (homeUiState?.value?.data) {
                        is HomeUiState.Disconnected -> {
                            connectVpn()
                        }

                        else -> {
                            Timber.d("HomeScreen: unknown = ${homeUiState?.value?.data}")
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (homeUiState?.value?.data) {
            is HomeUiState.Connected -> {
                ConnectedServer(
                    server = selectedServer ?: ServerModel(
                        countryName = "Unknown",
                        cityName = "",
                        flag = ""
                    ),
                    trafficStats =  trafficStats,
                    onClick = { onServerClick() }
                )
            }

            else -> {
                HomeServer(
                    server = selectedServer ?: ServerModel(
                        countryName = "Unknown",
                        cityName = "",
                        flag = ""
                    ),
                    onClick = { onServerClick() }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.img_premium_yellow),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = true) {
//                    navController.navigate(Destinations.PremiumScreen.route)
                    navController.navigate(Destinations.PremiumScreen.route.replace("{source}", PremiumSource.PREMIUM_BUTTON))
                },
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(16.dp))

        LoadNativeAd(
            context = context,
            adUnitId = admob_native_id_home,
            adSize = NativeAdSize.SMALL
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HomePreview() {
    val dummyServer = ServerModel(
        countryName = "Unknown",
        cityName = "",
        flag = ""
    )

    CatTheme {
        HomeContent(
            modifier = Modifier,
            navController = rememberNavController(),
            selectedServer = dummyServer,
            homeUiState = null,
            trafficStats = Stats(),
            onServerClick = {},
            connectVpn = {},
            disconnectVpn = {}
        )
    }
}