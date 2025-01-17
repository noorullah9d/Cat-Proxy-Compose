package ai.galixo.proxy.presentation.servers

import ai.galixo.proxy.R
import ai.galixo.proxy.presentation.components.FlagImage
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Surface
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
fun ServerItem(
    modifier: Modifier = Modifier,
    server: ServerModel,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(CatTheme.shape.tile)
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = CatTheme.colorScheme.divider,
                shape = CatTheme.shape.tile
            ),
        shape = CatTheme.shape.tile,
        color = CatTheme.colorScheme.background,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FlagImage(
                flagCode = server.flag,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = server.countryName,
                    style = CatTheme.typography.itemTitle,
                    color = CatTheme.colorScheme.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = server.ipaddress,
                    style = CatTheme.typography.regularText,
                    color = CatTheme.colorScheme.textTertiary
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_arrow_forward),
                contentDescription = "Forward"
            )
        }
        /*HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            color = CatTheme.colorScheme.divider
        )*/
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
            flag = "us"
        )
        ServerItem(
            server = dummyServer
        ) { }
    }
}