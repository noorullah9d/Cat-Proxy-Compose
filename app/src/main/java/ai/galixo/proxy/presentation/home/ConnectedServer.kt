package ai.galixo.proxy.presentation.home

import ai.galixo.proxy.R
import ai.galixo.proxy.domain.model.Stats
import ai.galixo.proxy.presentation.components.FlagImage
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.shadowsocks.preference.ServerModel

@Composable
fun ConnectedServer(
    modifier: Modifier = Modifier,
    server: ServerModel?,
    trafficStats: Stats,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(CatTheme.shape.tile)
            .fillMaxWidth()
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
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(8.dp), // Padding inside the row
            verticalAlignment = Alignment.CenterVertically
        ) {
            FlagImage(
                flagCode = server?.flag,
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 6.dp)
                    .size(32.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Server name
            Text(
                text = server!!.countryName,
                style = CatTheme.typography.itemTitle,
                color = CatTheme.colorScheme.textPrimary,
                modifier = Modifier.weight(1f) // Push the arrow to the end
            )

            Image(
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
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column( // ip
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(26.dp),
                    painter = painterResource(R.drawable.ic_ip),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = server!!.ipaddress,
                    maxLines = 1,
                    style = CatTheme.typography.semiBold,
                    color = CatTheme.colorScheme.textPrimary
                )
            }
            Column( // download
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(26.dp),
                    painter = painterResource(R.drawable.ic_download),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = trafficStats.downloadSpeed,
                    style = CatTheme.typography.semiBold,
                    color = CatTheme.colorScheme.textPrimary
                )
            }
            Column( // upload
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(26.dp),
                    painter = painterResource(R.drawable.ic_upload),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = trafficStats.uploadSpeed,
                    style = CatTheme.typography.semiBold,
                    color = CatTheme.colorScheme.textPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectedServerPreview() {
    CatTheme {
        ConnectedServer(
            server = ServerModel(
                countryName = "USA",
                flag = "us"
            ),
            trafficStats = Stats(),
            onClick = {}
        )
    }
}
