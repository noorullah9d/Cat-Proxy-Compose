package com.galixo.proxy.compose.ui.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.galixo.proxy.compose.domain.model.ServerModel
import com.galixo.proxy.compose.navigation.Destinations
import com.galixo.proxy.compose.theme.CatProxyTheme
import com.galixo.proxy.compose.theme.color_light_background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {

    val selectedServer by viewModel.selectedServer.collectAsState()
    val context = LocalContext.current

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
                selectedServer = selectedServer,
                context = context
            )
        }
    )
}

@Composable
fun HomeContent(
    modifier: Modifier,
    navController: NavController,
    selectedServer: ServerModel?,
    context: Context
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color_light_background)
    ) {
        // Sample server data
        val server = selectedServer ?: ServerModel(
            countryName = "Unknown",
            cityName = "",
            flag = ""
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

        ConnectButton(
            context = context,
            onClick = {
                Toast.makeText(context, "Not implemented!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    CatProxyTheme {
        val navController = rememberNavController() // Mock NavController for preview
        val context = LocalContext.current

        val server = ServerModel()
        HomeContent(
            modifier = Modifier,
            navController = navController,
            selectedServer = server,
            context = context
        )
    }
}