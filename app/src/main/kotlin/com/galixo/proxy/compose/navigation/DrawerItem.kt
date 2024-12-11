package com.galixo.proxy.compose.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.galixo.proxy.R
import com.galixo.proxy.compose.theme.color_light_text_primary
import com.galixo.proxy.compose.theme.color_light_text_secondary
import com.galixo.proxy.compose.utils.PrefUtils

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
                    style = MaterialTheme.typography.bodyLarge,
                    color = color_light_text_primary,
                    modifier = Modifier.weight(1f)
                )

                if (navigationItem.route == Destinations.LanguageScreen.route) {
                    Text(
                        text = PrefUtils.selectedLanguage?.languageName ?: "English",
                        style = MaterialTheme.typography.bodyMedium,
                        color = color_light_text_secondary,
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
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = navigationItem.icon),
                contentDescription = navigationItem.title
            )
        },
        modifier = Modifier
            .padding(NavigationDrawerItemDefaults.ItemPadding)
            .fillMaxWidth()
    )
}
