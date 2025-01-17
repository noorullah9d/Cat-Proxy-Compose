package ai.galixo.proxy.presentation.languages

import ai.galixo.proxy.R
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.languagesList
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.shadowsocks.preference.PrefUtils.selectedLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionSheet(
    onDismiss: (Boolean) -> Unit
) {
    val languages = languagesList()

    ModalBottomSheet(
        onDismissRequest = {onDismiss(false)},
        containerColor = CatTheme.colorScheme.background,
        dragHandle = { BottomSheetDefaults.DragHandle(color = CatTheme.colorScheme.divider) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title text
            Text(
                text = stringResource(R.string.language),
                style = CatTheme.typography.bold,
                color = CatTheme.colorScheme.textPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

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
                            onDismiss(true) // Close the bottom sheet after selection
                        }
                    )
                }
            }
        }
    }
}