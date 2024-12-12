package com.galixo.proxy.compose.ui.tunneling

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.galixo.proxy.R
import com.galixo.proxy.compose.domain.model.AppInfo
import com.galixo.proxy.compose.theme.CatProxyTheme
import com.galixo.proxy.compose.theme.color_light_background
import com.galixo.proxy.compose.ui.components.CenteredCircularProgressIndicator
import com.galixo.proxy.compose.ui.components.CustomTopAppBar
import com.galixo.proxy.compose.ui.components.SearchBar
import com.galixo.proxy.compose.utils.PrefUtils
import com.galixo.proxy.compose.utils.Result

@Composable
fun SplitTunnelingScreen(
    viewModel: SplitTunnelingViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {

    val appsState = viewModel.appsFlow.collectAsState()
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredApps = remember(appsState.value, searchQuery) {
        when (val state = appsState.value) {
            is Result.Success -> {
                state.data.filter { it.name.contains(searchQuery, ignoreCase = true) == true }
            }

            else -> emptyList()
        }
    }

    Scaffold(
        topBar = {
            Crossfade(targetState = isSearchActive, label = "") { active -> // animate UI changes
                if (active) {
                    SearchBar(
                        modifier = Modifier.padding(8.dp),
                        query = searchQuery,
                        onQueryChanged = {
                            searchQuery = it
                            viewModel.filterApps(it)
                        },
                        onClose = {
                            isSearchActive = false
                            searchQuery = ""
                            viewModel.resetApps()
                        }
                    )
                } else {
                    CustomTopAppBar(
                        title = stringResource(R.string.split_tunneling),
                        onBackPressed = onBackPressed,
                        menuItems = listOf(
                            {
                                IconButton(onClick = { isSearchActive = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
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
            AppsContent(
                modifier = Modifier.padding(paddingValues),
                appsState = appsState.value,
                filteredApps = filteredApps,
                onUpdateApp = { app, isAppHidden ->
                    val updatedApp = app.copy(isAppHidden = isAppHidden)
                    viewModel.updateAppState(updatedApp)
                }
            )
        }
    )
}

@Composable
fun AppsContent(
    modifier: Modifier = Modifier,
    appsState: Result<ArrayList<AppInfo>>?,
    filteredApps: List<AppInfo>,
    onUpdateApp: (AppInfo, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color_light_background)
            .padding(8.dp)
    ) {
//        Crossfade(targetState = appsState, label = "") { state ->
        when (/*state*/appsState) {
            is Result.Loading -> {
                CenteredCircularProgressIndicator()
            }

            is Result.Success -> {
                if (filteredApps.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No apps found!")
                    }
                } else {
                    LazyColumn(modifier) {
                        items(filteredApps) { app ->
                            AppItem(
                                appInfo = app,
                                onSwitchToggled = { isAppHidden ->
                                    // Handle switch toggle with PrefUtils
                                    if (!isAppHidden) {
                                        PrefUtils.removeDisableApp(app.packageName)
                                    } else {
                                        PrefUtils.addDisableApp(app.packageName)
                                    }
                                    onUpdateApp(
                                        app.copy(isAppHidden = isAppHidden),
                                        isAppHidden
                                    )
                                }
                            )
                        }
                    }
                }
            }

            is Result.Error -> {
                val errorMessage = /*state.message*/ appsState.message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: $errorMessage", color = Color.Red)
                }
            }

            null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Failed to fetch apps!", color = Color.Red)
                }
            }
        }
//        }
    }
}

@Composable
@Preview()
fun SplitTunnelingPreview() {
    CatProxyTheme {

        val context = LocalContext.current
        val dummyApps = arrayListOf(
            AppInfo(
                appIcon = ContextCompat.getDrawable(context, R.drawable.en),
                name = "App1",
                packageName = "com.example.app1",
                isAppHidden = false
            ),
            AppInfo(
                appIcon = ContextCompat.getDrawable(context, R.drawable.el),
                name = "App2",
                packageName = "com.example.app2",
                isAppHidden = false
            ),
            AppInfo(
                appIcon = ContextCompat.getDrawable(context, R.drawable.cs),
                name = "App3",
                packageName = "com.example.app3",
                isAppHidden = false
            )
        )

        // Example 1: Success state with a list of servers
        AppsContent(
            appsState = Result.Success(dummyApps),
            filteredApps = listOf(),
            onUpdateApp = {app, isHidden -> }
        )

        // Example 2: Loading state (Uncomment to see loading state)
        /*AppsContent(
            appsState = Result.Loading("")
        )*/

        // Example 3: Error state (Uncomment to see error state)
        /*AppsContent(
            appsState = Result.Error("Failed to load apps")
        )*/
    }
}