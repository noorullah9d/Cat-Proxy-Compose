package com.v2ray.ang.compose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v2ray.ang.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    onBackPressed: () -> Unit,
    menuItems: List<@Composable (() -> Unit)> = listOf()
) {
    TopAppBar(
        title = { Text(text = title, fontSize = 20.sp) },
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
        }
    )
}

@Preview(showBackground = false)
@Composable
fun CustomTopAppBarPreview() {
    // Preview for TopAppBar with back button and no menu items
    Column {
        CustomTopAppBar(
            title = "About",
            onBackPressed = { /* Handle back press */ }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Preview for TopAppBar with back button and a menu item (right end)
        CustomTopAppBar(
            title = "Settings",
            onBackPressed = { /* Handle back press */ },
            menuItems = listOf(
                {
                    IconButton(onClick = { /* Handle settings click */ }) {
                        Image(painter = painterResource(id = R.drawable.ic_about_24dp), contentDescription = "Settings")
                    }
                }
            )
        )
    }
}
