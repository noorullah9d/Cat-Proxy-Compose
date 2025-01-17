package ai.galixo.proxy.presentation.components

import ai.galixo.proxy.R
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.ui.theme.light_search_bar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    hint: String,
    shouldShowCross: Boolean = true,
    onQueryChanged: (String) -> Unit,
    onClose: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (shouldShowCross) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus() // Automatically focus the search field
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = light_search_bar,
                shape = CatTheme.shape.tile
            )
            .border(
                width = 1.dp,
                color = CatTheme.colorScheme.divider,
                shape = CatTheme.shape.tile
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton({}){
            Image(
                modifier = Modifier.size(28.dp),
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search"
            )
        }
        BasicTextField(
            value = query,
            onValueChange = { query ->
                onQueryChanged(query)
            },
            singleLine = true,
            textStyle = CatTheme.typography.medium.copy(
                color = CatTheme.colorScheme.textPrimary,
                fontSize = 16.sp
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide() // Hide keyboard when done
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
        ) { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = hint,
                        style = CatTheme.typography.medium.copy(
                            fontSize = 16.sp
                        ),
                        color = CatTheme.colorScheme.textTertiary
                    )
                }
                innerTextField() // Placeholder disappears when the user types
            }
        }
        if (shouldShowCross) {
            IconButton(onClick = onClose) {
                Image(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(R.drawable.ic_cross),
                    contentDescription = "Close"
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun SearchBarPreview() {
    CatTheme {
        SearchBar(
            query = "",
            hint = "Search...",
            onQueryChanged = {},
            onClose = {}
        )
    }
}