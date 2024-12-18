package com.galixo.proxy.compose.ui.servers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.galixo.proxy.R
import com.galixo.proxy.compose.admob.BannerAdView
import com.galixo.proxy.compose.admob.BannerShimmer
import com.galixo.proxy.compose.domain.model.ServerModel
import com.galixo.proxy.compose.theme.CatProxyTheme
import com.galixo.proxy.compose.theme.color_light_background
import com.galixo.proxy.compose.ui.components.CenteredCircularProgressIndicator
import com.galixo.proxy.compose.ui.components.CustomTopAppBar
import com.galixo.proxy.compose.utils.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServersScreen(
    viewModel: ServersViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val serverListState = viewModel.serverListState.collectAsState()

    // Swipe refresh state
    var isRefreshing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.locations),
                onBackPressed = onBackPressed,
                menuItems = listOf(
                    {
                        IconButton(onClick = { viewModel.fetchServers() }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_refresh),
                                contentDescription = "Refresh"
                            )
                        }
                    }
                )
            )
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
                    viewModel = viewModel,
                    onBackPressed = onBackPressed,
                    serverListState = serverListState.value
                )
            }
        }
    )
}

@Composable
fun ServersContent(
    modifier: Modifier = Modifier,
    viewModel: ServersViewModel?,
    onBackPressed: () -> Unit,
    serverListState: Result<List<ServerModel>>
) {
    var isAdLoading by remember { mutableStateOf(true) }
    var isAdVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color_light_background)
            .padding(8.dp)
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
                val serverList = serverListState.data
                LazyColumn(
                    modifier = modifier
                        .weight(1f) // Take up remaining space
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    items(serverList) { server ->
                        ServerItem(server = server) {
                            viewModel?.setSelectedServer(server)
                            onBackPressed()
                        }
                    }
                }
            }

            is Result.Error -> {
                val errorMessage = serverListState.message
                Box(
                    modifier = Modifier
                        .weight(1f) // Take up remaining space
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: $errorMessage", color = Color.Red)
                }
            }
        }

        if (isAdLoading) {
            BannerShimmer(
                modifier = Modifier.fillMaxWidth()
            )
        }

        BannerAdView(
            modifier = Modifier.fillMaxWidth(),
            onAdLoaded = { isAdLoading = false}, // Hide shimmer when ad loads
            onFailedToLoad = { isAdLoading = false}
        )
    }
}

@Composable
@Preview(showBackground = false)
fun ServersPreview() {
    CatProxyTheme {

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
            viewModel = null,
            onBackPressed = {},
            serverListState = Result.Success(dummyServers)
        )

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
