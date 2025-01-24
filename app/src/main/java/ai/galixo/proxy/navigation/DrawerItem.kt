package ai.galixo.proxy.navigation

import ai.galixo.proxy.R
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.shadowsocks.preference.PrefUtils

@Composable
fun DrawerItem(
    navigationItem: NavItem,
    isSelected: Boolean,
    onClick: (NavItem) -> Unit
) {
    NavigationDrawerItem(
        label = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = navigationItem.title,
                    style = CatTheme.typography.medium.copy(
                        fontSize = 16.sp
                    ),
                    color = CatTheme.colorScheme.textPrimary,
                    modifier = Modifier.weight(1f)
                )

                if (navigationItem.route == Destinations.LanguageSheet.route) {
                    Text(
                        text = PrefUtils.selectedLanguage?.languageName ?: "English",
                        style = CatTheme.typography.medium,
                        color = CatTheme.colorScheme.textSecondary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = "Forward"
                )
            }
        },
        selected = isSelected,
        onClick = { onClick(navigationItem) },
        icon = {
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = navigationItem.icon),
                contentDescription = navigationItem.title
            )
        },
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .fillMaxWidth()
    )
}

@Composable
@Preview(showBackground = true)
fun DrawerItemPreview() {
    CatTheme {
        val item = NavItem(
            title = "Name",
            icon = R.drawable.ic_languages,
            route = ""
        )

        DrawerItem(item, false) { }
    }
}
