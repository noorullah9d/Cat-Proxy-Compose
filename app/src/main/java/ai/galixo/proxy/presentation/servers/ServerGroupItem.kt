package ai.galixo.proxy.presentation.servers

import ai.galixo.proxy.R
import ai.galixo.proxy.presentation.components.FlagImage
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.GAME_FLAG
import ai.galixo.proxy.utils.SPORTS_FLAG
import ai.galixo.proxy.utils.STREAM_FLAG
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.shadowsocks.preference.ServerGroupModel
import com.github.shadowsocks.preference.ServerModel

@Composable
fun ServerGroupItem(
    modifier: Modifier = Modifier,
    group: ServerGroupModel,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    onUpdateServer: (ServerModel) -> Unit
) {
    Surface(
        modifier = modifier
            .clip(CatTheme.shape.tile)
            .clickable(onClick = onExpandToggle)
            .fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val flagImage = when (group.flag) {
                    GAME_FLAG -> R.drawable.ic_game_flag
                    STREAM_FLAG -> R.drawable.ic_stream_flag
                    SPORTS_FLAG -> R.drawable.ic_sports_flag
                    else -> null
                }
                if (flagImage != null) {
                    Image(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(id = flagImage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                } else {
                    FlagImage(
                        flagCode = group.flag,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = group.countryName,
                        style = CatTheme.typography.itemTitle,
                        color = CatTheme.colorScheme.textPrimary
                    )
                    Text(
                        text = stringResource(R.string.no_of_locations, group.data.size),
                        style = CatTheme.typography.regularText,
                        color = CatTheme.colorScheme.textTertiary
                    )
                }

                if (expanded) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_arrow_up),
                        contentDescription = "Collapse"
                    )
                } else {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = "Expand"
                    )
                }
            }
            if (expanded) {
                group.data.forEach { server ->
                    Spacer(Modifier.height(4.dp))
                    ServerItem(
                        server = server,
                        onClick = {
                            onUpdateServer(server)
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ServerGroupItemPreview() {
    CatTheme {

        val group = ServerGroupModel(
            countryName = "USA",
            flag = "us",
            data = arrayListOf(
                ServerModel(
                    id = 1,
                    countryName = "USA",
                    cityName = "New York",
                    ipaddress = "192.168.1.1",
                    flag = "us",
                ),
                ServerModel(
                    id = 1,
                    countryName = "USA",
                    cityName = "LA",
                    ipaddress = "192.168.1.1",
                    flag = "us",
                )
            )
        )

        ServerGroupItem(
            group = group,
            expanded = true,
            onUpdateServer = {},
            onExpandToggle = {}
        )
    }
}