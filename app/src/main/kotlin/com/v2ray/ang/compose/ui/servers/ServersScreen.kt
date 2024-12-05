package com.v2ray.ang.compose.ui.servers

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.v2ray.ang.R
import com.v2ray.ang.compose.domain.ServerModel
import com.v2ray.ang.compose.theme.CatProxyTheme
import com.v2ray.ang.compose.ui.components.CenteredCircularProgressIndicator
import com.v2ray.ang.compose.ui.components.CustomTopAppBar
import com.v2ray.ang.compose.utils.Result

@Composable
fun ServersScreen(
    viewModel: ServersViewModel = hiltViewModel(),
    onServerSelected: (ServerModel) -> Unit,
    onBackPressed: () -> Unit
) {
    val serverListState = viewModel.serverListState.collectAsState()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.locations),
                onBackPressed = onBackPressed,
                menuItems = listOf()
            )
        },
        content = { paddingValues ->
            ServersContent(
                modifier = Modifier.padding(paddingValues),
                onServerSelected = onServerSelected,
                serverListState = serverListState.value
            )
        }
    )
}

@Composable
fun ServersContent(
    modifier: Modifier = Modifier,
    onServerSelected: (ServerModel) -> Unit,
    serverListState: Result<List<ServerModel>>
) {
    when (serverListState) {
        is Result.Loading -> {
            // Show a loading spinner while data is being fetched
            CenteredCircularProgressIndicator()
        }

        is Result.Success -> {
            val serverList = serverListState.data
            LazyColumn(modifier) {
                items(serverList) { server ->
                    ServerItem(server = server) {
                        onServerSelected(server)
                    }
                }
            }
        }

        is Result.Error -> {
            val errorMessage = serverListState.message
            Text("Error: $errorMessage", color = Color.Red)
        }
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
            onServerSelected = {},
            serverListState = Result.Success(dummyServers)
        )

        // Example 2: Loading state (Uncomment to see loading state)
        /*ServersContent(
            onServerSelected = {},
            serverListState = Result.Loading
        )*/

        // Example 3: Error state (Uncomment to see error state)
        /*ServersContent(
            onServerSelected = {},
            serverListState = Result.Error("Failed to load servers")
        )*/
    }
}
