package ai.galixo.proxy.navigation

import ai.galixo.proxy.R
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.PremiumSource
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun DrawerHeader(
    navController: NavController,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .height(270.dp)
            .fillMaxWidth()
            .background(color = CatTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.more),
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f),
                style = CatTheme.typography.toolbarTitle
            )

            IconButton(
                onClick = onClose,
                modifier = Modifier
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Close Drawer"
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.img_premium_header),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clickable(enabled = true) {
//                    navController.navigate(Destinations.PremiumScreen.route)
                    navController.navigate(Destinations.PremiumScreen.route.replace("{source}", PremiumSource.PREMIUM_BUTTON))
                    onClose()
                },
            contentScale = ContentScale.FillWidth
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Composable
fun DrawerHeaderPreview() {
    CatTheme {
        DrawerHeader(
            navController = rememberNavController(),
            onClose = { /* Do nothing for preview */ }
        )
    }
}
