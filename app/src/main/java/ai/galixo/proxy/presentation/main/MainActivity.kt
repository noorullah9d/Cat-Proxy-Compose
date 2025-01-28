package ai.galixo.proxy.presentation.main

import ai.galixo.proxy.R
import ai.galixo.proxy.data.helper.VpnActivityHelper
import ai.galixo.proxy.navigation.AppNavigation
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.navigation.DrawerHeader
import ai.galixo.proxy.navigation.DrawerItem
import ai.galixo.proxy.navigation.getGroupedNavItems
import ai.galixo.proxy.presentation.base.BaseActivity
import ai.galixo.proxy.presentation.home.HomeViewModel
import ai.galixo.proxy.presentation.languages.LanguageSelectionSheet
import ai.galixo.proxy.presentation.servers.ServersViewModel
import ai.galixo.proxy.ui.theme.CatTheme
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.shadowsocks.preference.PrefUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val serversViewModel: ServersViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val activityHelper: VpnActivityHelper by lazy {
        VpnActivityHelper(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.getRemoteConfig() // get ad ids from remote config
        serversViewModel.fetchServers()
        activityHelper.initializeAndObserve()
        setContent {
            CatTheme {
                Surface {
                    MainScreen(
                        viewModel = homeViewModel,
                        onLanguageChanged = {
                            recreate()
                        }
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: HomeViewModel,
    onLanguageChanged: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val hasTermsAndConditionsAccepted = remember { PrefUtils.hasTermsAndConditionsAccepted }

    // State for Bottom Sheet
    var openLanguageSheet by rememberSaveable { mutableStateOf(false) }

    val navItems = getGroupedNavItems()

    // Track the current route (or destination)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Monitor the back press with a callback
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CatTheme.colorScheme.background)
                ) {
                    // Drawer Header
                    DrawerHeader(
                        navController = navController,
                        onClose = {
                            scope.launch { drawerState.close() }
                        }
                    )
                    Spacer(modifier = Modifier.height(0.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        navItems.forEach { group ->
                            item {
                                Text(
                                    text = group.groupName,
                                    style = CatTheme.typography.semiBold.copy(
                                        fontSize = 16.sp
                                    ),
                                    color = CatTheme.colorScheme.textTertiary,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                                )
                            }

                            item {
                                Surface(
                                    modifier = Modifier
                                        .clip(CatTheme.shape.tile)
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp)
                                        .border(
                                            width = 1.dp,
                                            color = CatTheme.colorScheme.divider,
                                            shape = CatTheme.shape.tile
                                        ),
                                    shape = CatTheme.shape.tile,
                                    color = CatTheme.colorScheme.background,
                                    shadowElevation = 2.dp,
                                ) {
                                    Column(
                                        modifier = Modifier
                                    ) {
                                        group.items.forEachIndexed  { index, navigationItem ->
                                            DrawerItem(
                                                navigationItem = navigationItem,
                                                isSelected = false,
                                                onClick = {
                                                    when (navigationItem.route) {
                                                        Destinations.LanguageSheet.route -> {
                                                            // Open the language selection sheet
                                                            openLanguageSheet = true
                                                        }
                                                        else -> {
                                                            // Navigate to the specified route
                                                            navController.navigate(navigationItem.route)
                                                        }
                                                    }
                                                    // Close the drawer after clicking
                                                    scope.launch { drawerState.close() }
                                                }
                                            )
                                            // Add divider conditionally based on index and number of items
                                            if (index < group.items.size - 1) {
                                                HorizontalDivider(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 16.dp),
                                                    color = CatTheme.colorScheme.divider
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        drawerState = drawerState,
        gesturesEnabled = false
    ) {
        Scaffold(
            topBar = {
                if (currentRoute == Destinations.HomeScreen.route) {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_app_name),
                                    contentDescription = "App Logo"
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch { drawerState.open() }
                                }
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_navigation),
                                    contentDescription = "Menu"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate(Destinations.PremiumScreen.route) }) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_premium),
                                    contentDescription = "Premium"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = CatTheme.colorScheme.background,
                        )
                    )
                }
            },
            content = {
                AppNavigation(
                    context = LocalContext.current,
                    viewModel = viewModel,
                    navController = navController,
                    hasTermsAndConditionsAccepted = hasTermsAndConditionsAccepted
                )
            }
        )
    }

    if (openLanguageSheet) {
        LanguageSelectionSheet { languageChanged ->
            openLanguageSheet = false
            if (languageChanged) onLanguageChanged()
        }
    }
}