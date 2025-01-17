package ai.galixo.proxy.presentation.servers

import ai.galixo.proxy.R
import ai.galixo.proxy.presentation.components.CenteredCircularProgressIndicator
import ai.galixo.proxy.presentation.components.CustomTopAppBar
import ai.galixo.proxy.presentation.components.SearchBar
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.Result
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.shadowsocks.preference.ServerModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServersScreen(
    viewModel: ServersViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val serverListState = viewModel.serverListState.collectAsState()

    // Swipe refresh state
    var isRefreshing by remember { mutableStateOf(false) }

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredServers = remember(serverListState.value, searchQuery) {
        when (val state = serverListState.value) {
            is Result.Success -> {
                state.data.filter { it.countryName.contains(searchQuery, ignoreCase = true) == true }
            }

            else -> emptyList()
        }
    }

    Scaffold(
        topBar = {
            Crossfade(targetState = isSearchActive, label = "") { active -> // animate UI changes
                if (active) {
                    SearchBar(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 14.dp),
                        query = searchQuery,
                        hint = stringResource(R.string.hint_locations),
                        onQueryChanged = { query ->
                            searchQuery = query
                            viewModel.filterServers(query)
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
        },
        content = { paddingValues ->
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    viewModel.fetchServers()
                    isRefreshing = false
                }
            ) {
                ServersContent(
                    modifier = Modifier.padding(paddingValues),
                    onBackPressed = onBackPressed,
                    serverListState = serverListState.value,
                    filteredServers = filteredServers,
                    onUpdateServer = { server ->
                        viewModel.setSelectedServer(server)
                    }
                )
            }
        }
    )
}

@Composable
fun ServersContent(
    modifier: Modifier = Modifier,
    serverListState: Result<List<ServerModel>>?,
    filteredServers: List<ServerModel>,
    onUpdateServer: (ServerModel) -> Unit,
    onBackPressed: () -> Unit,
) {
    var shouldShowShimmer by remember { mutableStateOf(true) }
    var shouldShowBannerAd by remember { mutableStateOf(true) }

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
                if (filteredServers.isEmpty()) {
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
                        items(filteredServers) { server ->
                            ServerItem(server = server) {
                                onUpdateServer(server)
                                onBackPressed()
                            }
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
            ServerModel(
                id = 1,
                countryName = "USA",
                cityName = "New York",
                ipaddress = "192.168.1.1",
                flag = "us"
            ),
            ServerModel(
                id = 2,
                countryName = "Germany",
                cityName = "Berlin",
                ipaddress = "192.168.1.2",
                flag = "de"
            ),
            ServerModel(
                id = 3,
                countryName = "France",
                cityName = "Paris",
                ipaddress = "192.168.1.3",
                flag = "fr"
            )
        )

        // Example 1: Success state with a list of servers
        ServersContent(
            serverListState = Result.Success(dummyServers),
            filteredServers = dummyServers,
            onUpdateServer = { }
        ) {}

        // Example 2: Loading state (Uncomment to see loading state)
        /*ServersContent(
            viewModel = null,
            onBackPressed = {},
            serverListState = Result.Loading("")
        )*/

        // Example 3: Error state (Uncomment to see error state)
        /*ServersContent(
            viewModel = null,
            onBackPressed = {},
            serverListState = Result.Error("Failed to load servers")
        )*/
    }
}
