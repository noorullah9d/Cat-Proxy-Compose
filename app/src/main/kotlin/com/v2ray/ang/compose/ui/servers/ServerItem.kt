package com.v2ray.ang.compose.ui.servers

import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.v2ray.ang.R
import com.v2ray.ang.compose.domain.model.ServerModel
import com.v2ray.ang.compose.theme.CatProxyTheme
import com.v2ray.ang.compose.theme.color_light_divider
import com.v2ray.ang.compose.theme.color_light_stroke
import com.v2ray.ang.compose.theme.color_light_text_secondary
import com.v2ray.ang.compose.ui.components.FlagImage

@Composable
fun ServerItem(server: ServerModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = server.ipaddress,
                    style = MaterialTheme.typography.bodySmall,
                    color = color_light_text_secondary
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_arrow_forward),
                contentDescription = "Forward",
                colorFilter = ColorFilter.tint(color_light_stroke)
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            color = color_light_divider
        )
    }
}

@Composable
@Preview
fun ServerPreview() {
    CatProxyTheme {
        val dummyServer = ServerModel(
            id = 1,
            countryName = "USA",
            cityName = "New York",
            ipaddress = "192.168.1.1",
            flag = "us"
        )
        ServerItem(dummyServer) { }
    }
}