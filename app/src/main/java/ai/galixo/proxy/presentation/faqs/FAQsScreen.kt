package ai.galixo.proxy.presentation.faqs

import ai.galixo.proxy.BuildConfig
import ai.galixo.proxy.R
import ai.galixo.proxy.presentation.components.CustomTopAppBar
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FAQsScreen(onBackPressed: () -> Unit) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                modifier = Modifier,
                title = stringResource(R.string.faqs),
                onBackPressed = onBackPressed,
                menuItems = listOf()
            )
        },
        content = { paddingValues ->
            FAQsContent(Modifier.padding(paddingValues))
        }
    )
}

@Composable
fun FAQsContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CatTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Version: ${BuildConfig.VERSION_NAME}",
            style = CatTheme.typography.medium,
            color = CatTheme.colorScheme.textPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    CatTheme {
        FAQsScreen {  }
    }
}