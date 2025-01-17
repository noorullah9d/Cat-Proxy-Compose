package ai.galixo.proxy.presentation.tunneling

import ai.galixo.proxy.R
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.presentation.languages.LanguageItem
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.languagesList
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.shadowsocks.preference.PrefUtils
import com.github.shadowsocks.preference.PrefUtils.selectedLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TunnelingInfoSheet(
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        containerColor = CatTheme.colorScheme.background,
        dragHandle = { BottomSheetDefaults.DragHandle(color = CatTheme.colorScheme.divider) }
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title text
            Text(
                text = stringResource(R.string.split_tunneling),
                style = CatTheme.typography.toolbarTitle,
                color = CatTheme.colorScheme.textPrimary,
                textAlign = TextAlign.Center,
            )

            Text(
                text = stringResource(R.string.tunneling_info_text),
                style = CatTheme.typography.regularText,
                color = CatTheme.colorScheme.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Button(
                onClick = { onDismiss() },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CatTheme.colorScheme.primary
                ),
                shape = CatTheme.shape.button
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 6.dp),
                    text = stringResource(R.string.close),
                    style = CatTheme.typography.buttonText,
                    color = CatTheme.colorScheme.onPrimary
                )
            }
        }
    }
}