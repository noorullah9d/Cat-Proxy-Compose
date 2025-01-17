package ai.galixo.proxy.presentation.tunneling

import ai.galixo.proxy.R
import ai.galixo.proxy.domain.model.AppInfo
import ai.galixo.proxy.presentation.components.CenteredCircularProgressIndicator
import ai.galixo.proxy.presentation.components.CustomTopAppBar
import ai.galixo.proxy.presentation.components.SearchBar
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.Result
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.shadowsocks.preference.PrefUtils

@Composable
fun SplitTunnelingScreen(
    viewModel: SplitTunnelingViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {

    val appsState = viewModel.appsFlow.collectAsState()
    // State for Bottom Sheet
    var openInfoSheet by rememberSaveable { mutableStateOf(false) }

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
            CustomTopAppBar(
                modifier = Modifier,
                title = stringResource(R.string.split_tunneling),
                onBackPressed = onBackPressed,
                menuItems = listOf(
                    {
                        IconButton(onClick = { openInfoSheet = true}) {
                            Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.ic_info),
                                contentDescription = "Info"
                            )
                        }
                    }
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CatTheme.colorScheme.background)
                    .padding(paddingValues)
            ) {
                SearchBar(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    query = searchQuery,
                    onQueryChanged = { query ->
                        searchQuery = query
                        viewModel.filterApps(query)
                    },
                    hint = stringResource(R.string.hint_apps),
                    shouldShowCross = false
                )
                AppsContent(
                    modifier = Modifier.weight(1f),
                    appsState = appsState.value,
                    filteredApps = filteredApps,
                    onUpdateApp = { app, isAppHidden ->
                        val updatedApp = app.copy(isAppHidden = isAppHidden)
                        viewModel.updateAppState(updatedApp)
                    }
                )
            }
        }
    )

    if (openInfoSheet) {
        TunnelingInfoSheet {
            openInfoSheet = false
        }
    }
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
            .background(CatTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        when (appsState) {
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
                    LazyColumn(
                        modifier = modifier
                            .weight(1f) // Take up remaining space
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
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
                val errorMessage = appsState.message
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CatTheme.colorScheme.errorSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: $errorMessage", color = CatTheme.colorScheme.onErrorSurface)
                }
            }

            null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CatTheme.colorScheme.errorSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Failed to fetch apps!", color = CatTheme.colorScheme.onErrorSurface)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SplitTunnelingPreview() {
    CatTheme {

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
            filteredApps = dummyApps,
            onUpdateApp = { app, isHidden -> }
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