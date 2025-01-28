package ai.galixo.proxy.presentation.servers

import ai.galixo.proxy.R
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.shadowsocks.preference.ServerModel

@Composable
fun ServerItem(
    modifier: Modifier = Modifier,
    server: ServerModel,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = CatTheme.colorScheme.background,
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = server.cityName,
                    style = CatTheme.typography.itemTitle,
                    color = CatTheme.colorScheme.textPrimary
                )

                if (server.isPremium) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_premium),
                        contentDescription = "Forward"
                    )
                } else {
                    Text(
                        text = stringResource(R.string.free),
                        style = CatTheme.typography.semiBold,
                        color = CatTheme.colorScheme.primary
                    )
                }

                Image(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = "Forward"
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                color = CatTheme.colorScheme.divider
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ServerPreview() {
    CatTheme {
        val dummyServer = ServerModel(
            id = 1,
            countryName = "USA",
            cityName = "New York",
            ipaddress = "192.168.1.1",
            flag = "us",
            isPremium = true
        )
        ServerItem(
            server = dummyServer
        ) { }
    }
}