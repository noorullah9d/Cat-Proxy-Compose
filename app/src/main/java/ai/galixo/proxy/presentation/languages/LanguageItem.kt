package ai.galixo.proxy.presentation.languages

import ai.galixo.proxy.R
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.shadowsocks.preference.LanguageModel

@Composable
fun LanguageItem(
    language: LanguageModel,
    isSelected: Boolean,
    onClick: (LanguageModel) -> Unit
) {
    // Modifier for selected state border color
    val borderColor = if (isSelected) CatTheme.colorScheme.primary else CatTheme.colorScheme.divider

    Surface(
        modifier = Modifier
            .clip(CatTheme.shape.tile)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = CatTheme.shape.tile
            ),
        shape = CatTheme.shape.tile,
        shadowElevation = 2.dp,
        color = CatTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(language) } // Click anywhere on the row to select/deselect
                .padding(12.dp), // Padding inside the row
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Language flag icon
            Image(
                painter = painterResource(id = language.flagDrawable),
                contentDescription = language.languageName,
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 6.dp)
                    .size(32.dp) // Adjust icon size if needed
                    .clip(CircleShape), // Make the flag rounded
                contentScale = ContentScale.Crop // Apply center crop effect
            )

            Spacer(modifier = Modifier.width(16.dp)) // Space between flag and name

            // Language name and native name
            Text(
                text = language.languageName,
                modifier = Modifier.weight(1f), // Push the radio button to the end
                style = CatTheme.typography.itemTitle,
                color = CatTheme.colorScheme.textPrimary
            )

            Text(
                text = "(${language.languageLocalName})",
                style = CatTheme.typography.normal,
                color = CatTheme.colorScheme.textTertiary,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LanguageItemPreview() {
    CatTheme {
        // Create a sample LanguageModel object
        val sampleLanguage = LanguageModel(
            languageName = "English",
            languageLocalName = "English",
            languageCode = "en",
            flagDrawable = R.drawable.en // Use a placeholder drawable for the flag (replace with your actual resource)
        )

        // Simulate that this language is selected
        LanguageItem(
            language = sampleLanguage,
            isSelected = true, // Set true to show the radio button as selected
            onClick = { /* Handle selection */ }
        )
    }
}