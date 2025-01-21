package ai.galixo.proxy.presentation.home

import ai.galixo.proxy.R
import ai.galixo.proxy.presentation.components.FlagImage
import ai.galixo.proxy.ui.theme.CatTheme
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
fun HomeServer(
    server: ServerModel?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp), // Padding inside the row
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
    }
}

@Preview(showBackground = true)
@Composable
fun HomeServerPreview() {
    CatTheme {
        HomeServer(
            server = ServerModel(
                countryName = "USA",
                flag = "us"
            ),
            onClick = {}
        )
    }
}
