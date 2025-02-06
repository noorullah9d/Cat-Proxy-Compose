package ai.galixo.proxy.presentation.servers

import ai.galixo.proxy.R
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.presentation.components.CenteredCircularProgressIndicator
import ai.galixo.proxy.presentation.components.CustomTopAppBar
import ai.galixo.proxy.presentation.components.SearchBar
import ai.galixo.proxy.presentation.home.HomeViewModel
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.PremiumSource
import ai.galixo.proxy.utils.Result
import ai.galixo.proxy.utils.extensions.toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.preference.PrefUtils
import com.github.shadowsocks.preference.ServerGroupModel
import com.github.shadowsocks.preference.ServerModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServersScreen(
    homeViewModel: HomeViewModel,
    viewModel: ServersViewModel = hiltViewModel(),
    navController: NavController,
    onBackPressed: () -> Unit
) {
    val serverListState = viewModel.serverListState.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(TabPage.All) }
    val context = LocalContext.current

    // Swipe refresh state
    var isRefreshing by remember { mutableStateOf(false) }

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Call the viewModel function to filter servers whenever the search query or tab changes
    LaunchedEffect(searchQuery, selectedTabIndex) {
        viewModel.filterServers(searchQuery, selectedTabIndex)
    }

    // Use LaunchedEffect to update isRefreshing after fetch completes
    LaunchedEffect(serverListState.value) {
        isRefreshing = false
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(CatTheme.colorScheme.background)
            ) {
                Crossfade(targetState = isSearchActive, label = "") { active -> // animate UI changes
                    if (active) {
                        SearchBar(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 14.dp),
                            query = searchQuery,
                            hint = stringResource(R.string.hint_locations),
                            onQueryChanged = { query ->
                                searchQuery = query
                            },
                            onClose = {
                                isSearchActive = false
                                searchQuery = ""
                                viewModel.resetServers()
                            }
                        )
                    } else {
                        CustomTopAppBar(
                            modifier = Modifier,
                            title = stringResource(R.string.locations),
                            onBackPressed = onBackPressed,
                            menuItems = listOf(
                                {
                                    IconButton(onClick = { isSearchActive = true }) {
                                        Image(
                                            modifier = Modifier.size(28.dp),
                                            painter = painterResource(R.drawable.ic_search),
                                            contentDescription = "Search"
                                        )
                                    }
                                }
                            )
                        )
                    }
                }
                CustomTabRow(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it }
                )
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        isRefreshing = true
                        viewModel.fetchServers()
                    }
                ) {
                    val alreadyConnected = stringResource(R.string.already_connected)
                    ServersContent(
                        serverListState = serverListState.value,
                        onUpdateServer = { server ->
                            if (PrefUtils.selectedProfile?.ipaddress == server.ipaddress  && BaseService.isStarted)
                                context.toast(alreadyConnected)
                            else if (server.isPremium) {
                                navController.navigate(Destinations.PremiumScreen.route.replace("{source}", PremiumSource.SERVERS))
                            } else {
                                viewModel.setSelectedServer(server)
                                homeViewModel.shouldReconnect(true)
                                onBackPressed()
                            }
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun ServersContent(
    modifier: Modifier = Modifier,
    serverListState: Result<List<ServerGroupModel>>?,
    onUpdateServer: (ServerModel) -> Unit,
) {
    var shouldShowShimmer by remember { mutableStateOf(true) }
    var shouldShowBannerAd by remember { mutableStateOf(true) }

    var expandedGroups = remember { mutableStateMapOf<String, Boolean>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CatTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        when (serverListState) {
            is Result.Loading -> {
                CenteredCircularProgressIndicator(
                    modifier = Modifier
                        .weight(1f) // Take up remaining space
                        .fillMaxSize()
                )
            }

            is Result.Success -> {
                if (serverListState.data.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Servers found!")
                    }
                } else {
                    LazyColumn(
                        modifier = modifier
                            .weight(1f) // Take up remaining space
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Timber.d("FilterServersList: ${serverListState.data}")
                        items(serverListState.data) { serverGroup  ->
                            ServerGroupItem(
                                group = serverGroup,
                                expanded = expandedGroups[serverGroup.countryName] == true,
                                onUpdateServer = onUpdateServer,
                                onExpandToggle = {
                                    expandedGroups[serverGroup.countryName] = expandedGroups[serverGroup.countryName] != true
                                }
                            )
                        }
                    }
                    shouldShowBannerAd = true
                }
            }

            is Result.Error -> {
                val errorMessage = serverListState.message
                Box(
                    modifier = Modifier
                        .weight(1f) // Take up remaining space
                        .fillMaxSize()
                        .background(CatTheme.colorScheme.errorSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: $errorMessage",
                        color = CatTheme.colorScheme.onErrorSurface
                    )
                }
                shouldShowBannerAd = false
                shouldShowShimmer = false
            }

            null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CatTheme.colorScheme.errorSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Failed to fetch servers!", color = CatTheme.colorScheme.onErrorSurface)
                }
            }
        }

        /*if (shouldShowShimmer) {
            BannerShimmer(
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (shouldShowBannerAd) {
            BannerAdView(
                modifier = Modifier.fillMaxWidth(),
                onAdLoaded = { shouldShowShimmer = false }, // Hide shimmer when ad loads
                onFailedToLoad = { shouldShowShimmer = false }
            )
        }*/
    }
}

@Composable
@Preview(showBackground = true)
fun ServersPreview() {
    CatTheme {

        // Example list of dummy server data
        val dummyServers = listOf(
            ServerGroupModel(
                countryName = "USA",
                flag = "us",
                data = arrayListOf(
                    ServerModel(
                        id = 1,
                        countryName = "USA",
                        cityName = "New York",
                        ipaddress = "192.168.1.1",
                        flag = "us",
                    ),
                    ServerModel(
                        id = 1,
                        countryName = "USA",
                        cityName = "LA",
                        ipaddress = "192.168.1.1",
                        flag = "us",
                    )
                )
            ),
            ServerGroupModel(
                countryName = "United Kingdom",
                flag = "gb",
                data = arrayListOf()
            )
        )

        // Example 1: Success state with a list of servers
        ServersContent(
            serverListState = Result.Success(dummyServers),
//            filteredServers = dummyServers,
            onUpdateServer = { }
        )

        // Example 2: Loading state (Uncomment to see loading state)
        /*ServersContent(
            viewModel = null,
            serverListState = Result.Loading("")
        )*/

        // Example 3: Error state (Uncomment to see error state)
        /*ServersContent(
            viewModel = null,
            serverListState = Result.Error("Failed to load servers")
        )*/
    }
}
