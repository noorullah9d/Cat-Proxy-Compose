package com.v2ray.ang.compose.ui.languages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.v2ray.ang.R
import com.v2ray.ang.compose.theme.CatProxyTheme
import com.v2ray.ang.compose.theme.color_light_background
import com.v2ray.ang.compose.utils.PrefUtils.selectedLanguage
import com.v2ray.ang.compose.utils.languagesList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionSheet(
    onDismiss: () -> Unit
) {
    // Get the list of languages
    val languages = languagesList()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        // Set custom background color for the bottom sheet
        containerColor = color_light_background, // Set the desired background color
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title text
            Text(
                text = stringResource(R.string.select_language),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Use LazyColumn to make the language list scrollable
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp) // Add spacing between items
            ) {
                items(languages) { language ->
                    LanguageItem(
                        language = language,
                        isSelected = selectedLanguage == language, // Check if the language is selected
                        onClick = {
                            selectedLanguage = language
                            onDismiss() // Close the bottom sheet after selection
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LanguageSelectionSheetPreview() {
    CatProxyTheme {
        LanguageSelectionSheet(onDismiss = {})
    }
}