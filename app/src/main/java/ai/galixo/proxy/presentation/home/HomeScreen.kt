package ai.galixo.proxy.presentation.home

import ai.galixo.proxy.R
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.presentation.components.LottieAnimation
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieConstants
import com.github.shadowsocks.preference.ServerModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val selectedServer by viewModel.selectedServer.collectAsState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(HomeUiState.Disconnected)
    val preConnectionState = remember { mutableStateOf<HomeUiState>(HomeUiState.Disconnected) }
    val context = LocalContext.current

    when (val state = uiState) {
        is HomeUiState.Disconnected -> {
            Timber.d("HomeScreen: vpn disconnected!")
        }

        is HomeUiState.Connecting -> {
            Timber.d("HomeScreen: vpn connecting...!")
        }

        is HomeUiState.Connected -> {
            Timber.d("HomeScreen: vpn connected!")
        }

        is HomeUiState.ConnectionFailed -> {
            Timber.d("HomeScreen: vpn connection failed!")
        }

        is HomeUiState.Time -> {
            Timber.d("HomeScreen: time = ${state.connectionTime}!")
        }

        is HomeUiState.Error -> {
            Timber.e("HomeScreen: error= ${state.message}!")
        }
        else -> {
            Timber.d("HomeScreen: else vpn disconnected! = $state")
        }
    }

    // collect pre-connection status
    LaunchedEffect(Unit) {
        viewModel.getPreConnectionStatus { state ->
            Timber.d("HomeScreen: getPreConnection = $state")
            preConnectionState.value = state
        }
    }

    Scaffold(
        topBar = {
            TopAppBar( // Dummy TopAppBar to occupy space
                title = { Spacer(modifier = Modifier) }
            )
        },
        content = { paddingValues ->
            DisconnectedContent(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                selectedServer = selectedServer,
                preConnectionState = preConnectionState,
                connectVpn = {
                    if (viewModel.isServerSelected()) viewModel.connect()
                    else Timber.d("HomeScreen: server not selected!")
                },
                disconnectVpn = {
                    Timber.d("HomeScreen: disconnectVpn")
                    viewModel.stopConnection()
                }
            )
        }
    )
}

@Composable
fun DisconnectedContent(
    modifier: Modifier,
    navController: NavController,
    selectedServer: ServerModel?,
    preConnectionState: MutableState<HomeUiState>?,
    connectVpn: () -> Unit,
    disconnectVpn: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CatTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sample server data
        val server = selectedServer ?: ServerModel(
            countryName = "Unknown",
            cityName = "",
            flag = ""
        )

        LottieAnimation(
            modifier = Modifier
                .size(250.dp)
                .weight(1f),
            animationRes = R.raw.anim_not_connected,
            iterations = LottieConstants.IterateForever
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = {
                when (preConnectionState?.value) {
                    is HomeUiState.Disconnected -> {
                        connectVpn()
                    }
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
            border = BorderStroke(1.dp, CatTheme.colorScheme.primary),
            shape = CatTheme.shape.button,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = CatTheme.colorScheme.background
            )
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                text = stringResource(R.string.tap_to_connect),
                style = CatTheme.typography.itemTitle,
                color = CatTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        HomeServer(
            server = server,
            onClick = {
                navController.navigate(Destinations.ServersScreen.route)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.img_premium_yellow),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = true) {
                    navController.navigate(Destinations.PremiumScreen.route)
                },
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .background(
                    color = CatTheme.colorScheme.adBackground,
                    shape = CatTheme.shape.ad
                )
        ) {}
    }
}

@Composable
@Preview
fun HomePreview() {

    // Sample server data
    val server = ServerModel(
        countryName = "Unknown",
        cityName = "",
        flag = ""
    )

    CatTheme {
        DisconnectedContent(
            modifier = Modifier,
            navController = rememberNavController(),
            selectedServer = server,
            preConnectionState = null,
            connectVpn = {},
            disconnectVpn = {}
        )
    }
}