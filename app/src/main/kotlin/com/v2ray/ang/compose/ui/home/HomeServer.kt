package com.v2ray.ang.compose.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.v2ray.ang.compose.domain.ServerModel
import com.v2ray.ang.compose.theme.color_light_stroke
import com.v2ray.ang.compose.ui.components.FlagImage

@Composable
fun HomeServer(
    server: ServerModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .border(
                width = 1.dp,
                color = color_light_stroke,
                shape = RoundedCornerShape(100.dp)
            )
            .padding(16.dp), // Padding inside the row
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Load the flag dynamically using FlagImage composable
        FlagImage(
            flagCode = server.flag,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Server name
        Text(
            text = server.countryName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f) // Push the arrow to the end
        )

        Image(
            painter = painterResource(id = R.drawable.ic_arrow_forward),
            contentDescription = "Forward",
            colorFilter = ColorFilter.tint(color_light_stroke)
        )
    }
}

@Preview(showBackground = false)
@Composable
fun HomeServerPreview() {
    HomeServer(
        server = ServerModel(
            countryName = "USA",
            flag = "us"
        ),
        onClick = {}
    )
}
