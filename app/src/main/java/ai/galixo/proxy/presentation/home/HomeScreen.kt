package ai.galixo.proxy.presentation.home

import ai.galixo.proxy.R
import ai.galixo.proxy.admob.admob_interstitial_id_disconnect
import ai.galixo.proxy.admob.admob_native_id_home
import ai.galixo.proxy.admob.admob_rewarded1_ad_id
import ai.galixo.proxy.admob.admob_rewarded2_ad_id
import ai.galixo.proxy.admob.admob_rewarded3_ad_id
import ai.galixo.proxy.admob.loadAdmobInterstitial
import ai.galixo.proxy.admob.loadRewardedAd
import ai.galixo.proxy.admob.nativeAd.LoadNativeAd
import ai.galixo.proxy.admob.nativeAd.NativeAdSize
import ai.galixo.proxy.domain.model.Stats
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.presentation.base.UiState
import ai.galixo.proxy.presentation.components.GradientButton
import ai.galixo.proxy.presentation.components.LottieAnimation
import ai.galixo.proxy.presentation.dialogs.DisconnectVpnDialog
import ai.galixo.proxy.presentation.dialogs.LoadingRewardProgressDialog
import ai.galixo.proxy.presentation.dialogs.NoInternetDialog
import ai.galixo.proxy.presentation.dialogs.NoServerFoundDialog
import ai.galixo.proxy.presentation.dialogs.OfferDialog
import ai.galixo.proxy.presentation.main.getActivity
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.ui.theme.lightOrange
import ai.galixo.proxy.ui.theme.orange
import ai.galixo.proxy.utils.GOT_REWARD_TIME
import ai.galixo.proxy.utils.PremiumSource
import ai.galixo.proxy.utils.extensions.isOnline
import ai.galixo.proxy.utils.extensions.toast
import ai.galixo.proxy.utils.isShowingAd
import ai.galixo.proxy.utils.postAnalytics
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieConstants
import com.github.shadowsocks.extensions.formatToTime
import com.github.shadowsocks.preference.PrefUtils.freeVpnTime
import com.github.shadowsocks.preference.PrefUtils.isAdsRemoved
import com.github.shadowsocks.preference.ServerModel
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardedAd
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
    val activity = getActivity()
    val progressToast = stringResource(R.string.connection_in_progress)
    val noInternetToast = stringResource(R.string.no_internet_connection)

    var showDisconnectDialog by remember { mutableStateOf(false) }
    var showOfferDialog by remember { mutableStateOf(false) }
    var isAlreadyConnected by remember { mutableStateOf(false) }
    var showProgressDialog by remember { mutableStateOf(false) }
    var showNoInternetDialog by remember { mutableStateOf(false) }
    var showNoServerFoundDialog by remember { mutableStateOf(false) }
    var retryAction by remember { mutableStateOf<() -> Unit>({}) } // Store the retry function dynamically
    var watchedAdsCount by remember { mutableIntStateOf(0) }
    var totalAdsToWatch by remember { mutableIntStateOf(0) }
    var rewardedAd: RewardedAd? by remember { mutableStateOf(null) }
    var interstitialAd: InterstitialAd? by remember { mutableStateOf(null) }

    // Ad IDs in sequence based on totalRewards
    val adIds = listOf(
        admob_rewarded1_ad_id,
        admob_rewarded2_ad_id,
        admob_rewarded3_ad_id
    )

    fun loadRewardedAd() {
        if (watchedAdsCount < totalAdsToWatch) {
            val adUnitId = adIds[watchedAdsCount] // Use corresponding ad ID
            activity?.loadRewardedAd(
                adUnitId = adUnitId,
                onAdFailedToLoad = { error ->
                    context.toast("Failed to load ad: ${error.message}")
                    rewardedAd = null
                    showProgressDialog = false
                },
                onAdLoaded = { ad ->
                    rewardedAd = ad
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            isShowingAd = true // Set flag to prevent other ads
                        }

                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            isShowingAd = false // Reset flag

                            watchedAdsCount++
                            if (watchedAdsCount < totalAdsToWatch) {
                                loadRewardedAd() // Load next ad
                            } else {
                                showProgressDialog = false
                                viewModel.addFreeVpnTime(totalAdsToWatch)
                                context.postAnalytics(GOT_REWARD_TIME)
                                context.toast(
                                    context.getString(
                                        R.string.free_vpn_time_congratulations,
                                        freeVpnTime.formatToTime()
                                    )
                                )
                                if (!isAlreadyConnected) viewModel.connect()
                            }
                        }
                    }
                }
            )
        }
    }

    fun loadDisconnectInterstitial() {
        activity?.loadAdmobInterstitial(
            admob_interstitial_id_disconnect,
            onAdFailedToLoad = { interstitialAd = null },
            onAdLoaded = { ad ->
                interstitialAd = ad
                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        isShowingAd = true // Set flag to prevent other ads
                    }

                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        isShowingAd = false
                        interstitialAd = null
                    }
                }
            }
        )
    }

    fun handleReconnection() {
        viewModel.expireFreeVpnTimeIfNeeded()
        if (!isAdsRemoved && freeVpnTime <= 2000L) { // at least 2sec freeVpnTime is required
            showOfferDialog = true
            isAlreadyConnected = false
        } else {
            viewModel.shouldReconnect(false)
            viewModel.reconnect()
        }
    }

    fun handleConnection() {
        viewModel.expireFreeVpnTimeIfNeeded()
        Timber.d("HomeScreen: free vpn time = $freeVpnTime")
        if (!isAdsRemoved && freeVpnTime <= 2000L) { // at least 2sec freeVpnTime is required
            showOfferDialog = true
            isAlreadyConnected = false
        } else viewModel.connect()
    }

    fun handlePreConnection() {
        viewModel.getPreConnectionStatus {
            if (it == HomeUiState.Connecting) {
                viewModel.stopConnection()
            } else {
                if (context.isOnline()) {
                    handleConnection()
                } else {
                    showNoInternetDialog = true
                    retryAction = { handleConnection() }
                }
            }
        }
    }

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
        Timber.d("HomeScreen: shouldReconnect= $shouldReconnect")
        if (shouldReconnect) {
            if (context.isOnline()) {
                handleReconnection()
            } else {
                showNoInternetDialog = true
                retryAction = { handleReconnection() }
            }
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
                    if (viewModel.isServerSelected()) {
                        handlePreConnection()
                    } else {
                        showNoServerFoundDialog = true
                    }
                },
                disconnectVpn = {
                    showDisconnectDialog = true
                    if (!isAdsRemoved) loadDisconnectInterstitial()
                },
                onPremiumClick = {
                    navController.navigate(
                        Destinations.PremiumScreen.route.replace(
                            "{source}",
                            PremiumSource.PREMIUM_BUTTON
                        )
                    )
                },
                onGetExtraTimeClick = {
                    showOfferDialog = true
                    isAlreadyConnected = true
                }
            )
        }
    )

    // Offer Dialog
    if (showOfferDialog) {
        OfferDialog(
            onDismiss = { showOfferDialog = false },
            onOfferSelected = { adsRequired ->
                totalAdsToWatch = adsRequired
                watchedAdsCount = 0
                showOfferDialog = false
                showProgressDialog = true
                loadRewardedAd() // Start loading first ad
            },
            onPremiumClicked = {
                navController.navigate(
                    Destinations.PremiumScreen.route.replace(
                        "{source}",
                        PremiumSource.PREMIUM_BUTTON
                    )
                )
            }
        )
    }

    // Progress Dialog
    if (showProgressDialog) {
        LoadingRewardProgressDialog(
            completedRewards = watchedAdsCount,
            totalRewards = totalAdsToWatch,
            onDismiss = {},
            onRewardComplete = {}
        )
    }

    // Show the rewarded ad when available
    LaunchedEffect(rewardedAd) {
        rewardedAd?.let { ad ->
            activity?.let {
                if (!isShowingAd) ad.show(it) {}
            }
        }
    }

    if (showDisconnectDialog) {
        DisconnectVpnDialog(
            onDismiss = { showDisconnectDialog = false },
            onDisconnectClick = {
                showDisconnectDialog = false
                interstitialAd?.let { ad ->
                    activity?.let {
                        if (!isShowingAd) ad.show(it)
                    }
                }
                viewModel.stopConnection()
            }
        )
    }

    if (showNoInternetDialog) {
        NoInternetDialog(
            onDismiss = { showNoInternetDialog = false },
            onTryAgainClick = {
                if (context.isOnline()) retryAction()
                else context.toast(noInternetToast)
            }
        )
    }

    if (showNoServerFoundDialog) {
        NoServerFoundDialog(
            onDismiss = { showNoServerFoundDialog = false },
            onTryAgainClick = { handlePreConnection() }
        )
    }
}

@Composable
fun HomeContent(
    modifier: Modifier,
    selectedServer: ServerModel?,
    homeUiState: MutableState<UiState<HomeUiState>>?,
    trafficStats: Stats,
    onServerClick: () -> Unit,
    connectVpn: () -> Unit,
    disconnectVpn: () -> Unit,
    onPremiumClick: () -> Unit,
    onGetExtraTimeClick: () -> Unit
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
            iterations = LottieConstants.IterateForever,
            onAnimationEnd = {}
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
                    style = CatTheme.typography.timerText,
                    color = CatTheme.colorScheme.textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                GradientButton(
                    text = stringResource(R.string.get_extra_time),
                    startColor = orange,
                    endColor = lightOrange,
                    onClick = {
                        onGetExtraTimeClick()
                    }
                )
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
                    trafficStats = trafficStats,
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
        when (homeUiState?.value?.data) {
            is HomeUiState.Connected -> {
                DisconnectButton { disconnectVpn() }
            }

            else -> {
                Image(
                    painter = painterResource(id = R.drawable.img_premium_yellow),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = true) {
                            onPremiumClick.invoke()
                        },
                    contentScale = ContentScale.FillWidth
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
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
            selectedServer = dummyServer,
            homeUiState = null,
            trafficStats = Stats(),
            onServerClick = {},
            connectVpn = {},
            disconnectVpn = {},
            onPremiumClick = {},
            onGetExtraTimeClick = {}
        )
    }
}