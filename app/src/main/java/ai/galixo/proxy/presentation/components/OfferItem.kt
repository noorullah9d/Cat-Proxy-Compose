package ai.galixo.proxy.presentation.components

import ai.galixo.proxy.R
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OfferItem(
    @DrawableRes iconRes: Int,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, CatTheme.colorScheme.divider),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CatTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = CatTheme.typography.bold.copy(
                        fontSize = 16.sp
                    ),
                    color = CatTheme.colorScheme.textPrimary
                )
                Text(
                    text = description,
                    style = CatTheme.typography.regularText,
                    color = CatTheme.colorScheme.textTertiary
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
    }
}

@Preview(showBackground = true)
@Composable
fun OfferItemPreview() {
    CatTheme {
        OfferItem(
            iconRes = R.drawable.img_30min,
            title = "30 Minutes",
            description = "Watch 1 ad, earn 30 min full access",
            onClick = {}
        )
    }
}
