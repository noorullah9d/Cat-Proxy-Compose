package ai.galixo.proxy.presentation.faqs

import ai.galixo.proxy.R
import ai.galixo.proxy.domain.model.FAQItem
import ai.galixo.proxy.presentation.components.CustomTopAppBar
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FAQsScreen(
    viewModel: FAQsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
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
            FAQsContent(
                modifier = Modifier.padding(paddingValues),
                faqs = viewModel.faqs
            )
        }
    )
}

@Composable
fun FAQsContent(
    modifier: Modifier = Modifier,
    faqs: List<FAQItem>
) {
    // Holds the topic that is currently expanded to show its body.
    var expandedTopic by remember { mutableStateOf<FAQItem?>(null) }
    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CatTheme.colorScheme.background)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(CatTheme.shape.tile)
                .border(
                    width = 1.dp,
                    color = CatTheme.colorScheme.divider,
                    shape = CatTheme.shape.tile
                ),
            color = CatTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CatTheme.colorScheme.background)
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = lazyListState
            ) {
                item {
                    faqs.forEachIndexed { index, faq ->
                        FAQItemView(
                            faq = faq,
                            expanded = expandedTopic == faq,
                            onClick = {
                                expandedTopic = if (expandedTopic == faq) null else faq
                            }
                        )

                        // Add divider conditionally based on index and number of items
                        if (index < faqs.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                color = CatTheme.colorScheme.divider
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    CatTheme {
        FAQsScreen { }
    }
}