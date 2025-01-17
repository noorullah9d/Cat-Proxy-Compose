package ai.galixo.proxy.presentation.components

import ai.galixo.proxy.R
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    modifier: Modifier,
    title: String,
    onBackPressed: () -> Unit,
    menuItems: List<@Composable (() -> Unit)> = listOf()
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = title,
                style = CatTheme.typography.toolbarTitle,
                color = CatTheme.colorScheme.textPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            menuItems.forEach { menuItem ->
                menuItem()
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CatTheme.colorScheme.background
        )
    )
}

@Preview(showBackground = false)
@Composable
fun CustomTopAppBarPreview() {
    CatTheme {
        // Preview for TopAppBar with back button and no menu items
        Column {
            CustomTopAppBar(
                modifier = Modifier,
                title = "About",
                onBackPressed = { /* Handle back press */ }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Preview for TopAppBar with back button and a menu item (right end)
            CustomTopAppBar(
                modifier = Modifier,
                title = "Settings",
                onBackPressed = { /* Handle back press */ },
                menuItems = listOf(
                    {
                        IconButton(onClick = { /* Handle settings click */ }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_info),
                                contentDescription = ""
                            )
                        }
                    }
                )
            )
        }
    }
}
