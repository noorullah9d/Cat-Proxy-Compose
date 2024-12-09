package com.v2ray.ang.compose.ui.languages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.v2ray.ang.R
import com.v2ray.ang.compose.domain.model.LanguageModel
import com.v2ray.ang.compose.theme.color_light_primary
import com.v2ray.ang.compose.theme.color_light_tile
import com.v2ray.ang.compose.theme.color_light_unselected

@Composable
fun LanguageItem(
    language: LanguageModel,
    isSelected: Boolean,
    onClick: (LanguageModel) -> Unit
) {
    // Modifier for selected state border color
    val borderColor = if (isSelected) color_light_primary else Color.Transparent

    Row(
        modifier = Modifier
            .background(
                color = color_light_tile,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .clickable { onClick(language) } // Click anywhere on the row to select/deselect
            .border(
                1.dp,
                borderColor,
                RoundedCornerShape(16.dp)
            ) // Apply border with primary color for selected item
            .padding(8.dp), // Padding inside the row
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Language flag icon
        Image(
            painter = painterResource(id = language.flagDrawable),
            contentDescription = language.languageName,
            modifier = Modifier
                .padding(start = 4.dp)
                .size(32.dp) // Adjust icon size if needed
                .clip(CircleShape), // Make the flag rounded
            contentScale = ContentScale.Crop // Apply center crop effect
        )

        Spacer(modifier = Modifier.width(16.dp)) // Space between flag and name

        // Language name and native name
        Text(
            text = "${language.languageName} (${language.languageLocalName})",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f) // Push the radio button to the end
        )

        // RadioButton for selection at the end
        RadioButton(
            selected = isSelected,
            onClick = {}, // Update the selected language
            colors = RadioButtonDefaults.colors(
                selectedColor = color_light_primary, // Custom color for selected state
                unselectedColor = color_light_unselected // Custom color for unselected state
            ),
            modifier = Modifier.padding(0.dp) // Remove default padding
        )
    }
}


@Preview(showBackground = false)
@Composable
fun LanguageItemPreview() {
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
