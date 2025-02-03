package ai.galixo.proxy.admob.nativeAd

import ai.galixo.proxy.admob.shimmerEffect
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SmallNativeShimmer() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(CardDefaults.shape),
        colors = CardDefaults.cardColors(
            containerColor = CatTheme.colorScheme.adBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(65.dp)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(20.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .shimmerEffect()
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                            .shimmerEffect()
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(120.dp)
                    .height(45.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .shimmerEffect()
            )
        }
    }
}



@Preview(showBackground = false)
@Composable
fun NativeShimmerPreview() {
    CatTheme {
        SmallNativeShimmer()
    }
}

