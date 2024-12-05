package com.v2ray.ang.compose.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.v2ray.ang.compose.domain.ServerModel
import com.v2ray.ang.compose.navigation.Destinations
import com.v2ray.ang.compose.theme.CatProxyTheme
import com.v2ray.ang.compose.theme.color_light_background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    var selectedServer by remember { mutableStateOf<ServerModel?>(null) }

    // Retrieve the selected server if available from the saved state
    LaunchedEffect(navController) {
        navController.currentBackStackEntry?.savedStateHandle?.get<ServerModel>("selected_server")
            ?.let {
                selectedServer = it
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
                navController = navController,
                selectedServer = selectedServer
            )
        }
    )
}

@Composable
fun HomeContent(
    modifier: Modifier,
    navController: NavController,
    selectedServer: ServerModel?
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color_light_background)
    ) {
        // Sample server data
        val server = selectedServer ?: ServerModel(
            countryName = "USA",
            cityName = "New York",
            flag = "us"
        )

        HomeServer(
            server = server,
            onClick = {
                navController.navigate(Destinations.ServersScreen.route)
            },
            modifier = Modifier
                .align(Alignment.TopCenter) // Align to top center
                .padding(top = 32.dp, start = 32.dp, end = 32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    CatProxyTheme {
        val navController = rememberNavController() // Mock NavController for preview
        HomeScreen(navController)
    }
}