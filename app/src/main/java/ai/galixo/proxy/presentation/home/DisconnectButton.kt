package ai.galixo.proxy.presentation.home

import ai.galixo.proxy.R
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DisconnectButton(
    disconnectVpn: () -> Unit,
) {
    OutlinedButton(
        onClick = { disconnectVpn() },
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
        border = BorderStroke(1.dp, CatTheme.colorScheme.divider),
        shape = CatTheme.shape.button,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = CatTheme.colorScheme.background
        )
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            text = stringResource(R.string.disconnect),
            style = CatTheme.typography.buttonText,
            color = CatTheme.colorScheme.primary
        )
    }
}

@Composable
@Preview
fun DisconnectButtonPreview() {
    CatTheme {
        DisconnectButton { }
    }
}