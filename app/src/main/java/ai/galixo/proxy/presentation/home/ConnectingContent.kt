package ai.galixo.proxy.presentation.home

import ai.galixo.proxy.R
import ai.galixo.proxy.presentation.components.LottieAnimation
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieConstants
import com.github.shadowsocks.preference.ServerModel
import timber.log.Timber

@Composable
fun ConnectingContent(
    modifier: Modifier,
    selectedServer: ServerModel?
) {
    Timber.d("HomeScreen: Recomposing ConnectingContent")
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CatTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(250.dp)
                .weight(1f),
            animationRes = R.raw.anim_connecting,
            iterations = LottieConstants.IterateForever
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .padding(bottom = 10.dp),
            border = BorderStroke(1.dp, CatTheme.colorScheme.primary),
            shape = CatTheme.shape.button,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = CatTheme.colorScheme.background
            )
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                text = stringResource(R.string.connecting),
                style = CatTheme.typography.itemTitle,
                color = CatTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        HomeServer(
            server = selectedServer ?: ServerModel(
                countryName = "Unknown",
                cityName = "",
                flag = ""
            ),
            onClick = {}
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.img_premium_yellow),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .background(
                    color = CatTheme.colorScheme.adBackground,
                    shape = CatTheme.shape.ad
                )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectingContentPreview() {

    val dummyServer = ServerModel(
        countryName = "Unknown",
        cityName = "",
        flag = ""
    )

    CatTheme {
        ConnectingContent(
            modifier = Modifier,
            selectedServer = dummyServer
        )
    }
}
