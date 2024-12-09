package com.v2ray.ang.compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.v2ray.ang.R
import com.v2ray.ang.compose.navigation.AppNavigation
import com.v2ray.ang.compose.navigation.Destinations
import com.v2ray.ang.compose.navigation.DrawerHeader
import com.v2ray.ang.compose.navigation.DrawerItem
import com.v2ray.ang.compose.navigation.getNavItems
import com.v2ray.ang.compose.theme.CatProxyTheme
import com.v2ray.ang.compose.theme.color_light_background
import com.v2ray.ang.compose.ui.languages.LanguageSelectionSheet
import com.v2ray.ang.compose.ui.servers.ServersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ComposeMainActivity : BaseActivity() {
    private val serversViewModel: ServersViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        serversViewModel.fetchServers()
        setContent {
            CatProxyTheme {
                Surface {
                    MainScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    // State for Bottom Sheet
    var openLanguageSheet by rememberSaveable { mutableStateOf(false) }

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
                        .fillMaxHeight()
                        .background(color_light_background)
                ) {
                    // Drawer Header
                    DrawerHeader(
                        onClose = {
                            scope.launch { drawerState.close() }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    getNavItems().forEachIndexed { index, navigationItem ->
                        DrawerItem(
                            navigationItem = navigationItem,
                            isSelected = false,
                            onClick = {
                                when (navigationItem.route) {
                                    Destinations.LanguageScreen.route -> {
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
                            Image(
                                painter = painterResource(id = R.drawable.ic_app_name), // Use your logo or image resource here
                                contentDescription = "App Logo" // Adjust size and layout as needed
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
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
                        }
                    )
                }
            },
            content = {
                AppNavigation(navController)
            }
        )
    }

    if (openLanguageSheet) {
        LanguageSelectionSheet {
            openLanguageSheet = false
            // TODO: restart the app
        }
    }
}
