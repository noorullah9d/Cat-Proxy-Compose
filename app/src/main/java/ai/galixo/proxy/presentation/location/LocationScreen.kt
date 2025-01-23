package ai.galixo.proxy.presentation.location

import ai.galixo.proxy.R
import ai.galixo.proxy.domain.model.LocationData
import ai.galixo.proxy.presentation.components.CenteredCircularProgressIndicator
import ai.galixo.proxy.presentation.components.CustomTopAppBar
import ai.galixo.proxy.presentation.components.FlagImage
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.Result
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("MissingPermission")
@Composable
fun LocationScreen(
    viewModel: LocationViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val locationData by viewModel.locationData.collectAsState()

    // Fetch location data when the screen is created
    LaunchedEffect(Unit) {
        viewModel.fetchLocationData()
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                modifier = Modifier,
                title = stringResource(R.string.check_current_location),
                onBackPressed = onBackPressed,
                menuItems = listOf()
            )
        },
        content = { paddingValues ->
            LocationContent(
                modifier = Modifier.padding(paddingValues),
                locationData = locationData
            )
        }
    )
}

@Composable
fun LocationContent(
    modifier: Modifier,
    locationData: Result<LocationData>
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when(locationData) {
            is Result.Loading -> {
                CenteredCircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is Result.Success -> {
                // Google Map and Card for Location details
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(
                                LatLng(locationData.data.latitude ?: 0.0, locationData.data.longitude ?: 0.0),
                                10f
                            )
                        }
                    ) {
                        locationData.data.latitude?.let { latitude ->
                            locationData.data.longitude?.let { longitude ->
                                Marker(state = MarkerState(position = LatLng(latitude, longitude)))
                            }
                        }
                    }

                    // Card with location details
                    LocationDetailsCard(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        locationData = locationData.data
                    )
                }
            }
            is Result.Error -> {
                Text(
                    text = locationData.message,
                    color = CatTheme.colorScheme.onErrorSurface,
                    modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                    style = CatTheme.typography.medium.copy(
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}

@Composable
fun LocationDetailsCard(
    modifier: Modifier,
    locationData: LocationData
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = CatTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.current_location),
                style = CatTheme.typography.toolbarTitle,
                color = CatTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Column(
                modifier = Modifier
                    .clip(CatTheme.shape.tile)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(
                        shape = CatTheme.shape.tile,
                        color = CatTheme.colorScheme.background
                    )
                    .border(
                        width = 1.dp,
                        color = CatTheme.colorScheme.divider,
                        shape = CatTheme.shape.tile
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), // Padding inside the row
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FlagImage(
                        flagCode = locationData.flag,
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 6.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Server name
                    Text(
                        text = locationData.locationName,
                        style = CatTheme.typography.itemTitle,
                        color = CatTheme.colorScheme.textPrimary,
                        modifier = Modifier.weight(1f) // Push the arrow to the end
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_forward),
                        contentDescription = "Forward"
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "IP Address",
                            maxLines = 1,
                            style = CatTheme.typography.medium.copy(
                                fontSize = 14.sp
                            ),
                            color = CatTheme.colorScheme.textSecondary,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = locationData.ipAddress,
                            maxLines = 1,
                            style = CatTheme.typography.semiBold.copy(
                                fontSize = 14.sp
                            ),
                            color = CatTheme.colorScheme.textPrimary
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        color = CatTheme.colorScheme.divider
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Time Zone",
                            maxLines = 1,
                            style = CatTheme.typography.medium.copy(
                                fontSize = 14.sp
                            ),
                            color = CatTheme.colorScheme.textSecondary,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = locationData.timeZone,
                            maxLines = 1,
                            style = CatTheme.typography.semiBold.copy(
                                fontSize = 14.sp
                            ),
                            color = CatTheme.colorScheme.textPrimary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationScreenPreview() {
    // Mocking the LocationData for Success state
    val locationDataSuccess = LocationData(
        locationName = "San Francisco",
        ipAddress = "192.168.1.1",
        timeZone = "PST",
        flag = "US",
        latitude = 37.7749,
        longitude = -122.4194
    )

    CatTheme {
        LocationContent(
            modifier = Modifier,
            locationData = Result.Success(locationDataSuccess),
        )
    }
}