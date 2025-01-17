package ai.galixo.proxy.presentation.tunneling

import ai.galixo.proxy.R
import ai.galixo.proxy.domain.model.AppInfo
import ai.galixo.proxy.presentation.components.CustomSwitch
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

@Composable
fun AppItem(
    appInfo: AppInfo,
    onSwitchToggled: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(CatTheme.shape.tile)
            .fillMaxWidth()
            .border(
                width = 0.5.dp,
                color = CatTheme.colorScheme.divider,
                shape = CatTheme.shape.tile
            ),
        shape = CatTheme.shape.tile,
        shadowElevation = 2.dp,
        color = CatTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIcon(
                drawable = appInfo.appIcon,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = appInfo.name,
                    style = CatTheme.typography.medium.copy(
                        fontSize = 18.sp
                    ),
                    color = CatTheme.colorScheme.textPrimary
                )
            }

            CustomSwitch(
                checked = !appInfo.isAppHidden,
                onCheckedChange = { isChecked ->
                    onSwitchToggled(!isChecked) // Update isAppHidden state
                },
                thumbColor = CatTheme.colorScheme.onPrimary,
                thumbUncheckedColor = CatTheme.colorScheme.onPrimary,
                trackCheckedColor = CatTheme.colorScheme.primary,
                trackUncheckedColor = CatTheme.colorScheme.divider
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AppItemPreview() {
    CatTheme {
        val context = LocalContext.current
        val app = AppInfo(
            appIcon = ContextCompat.getDrawable(context, R.drawable.en),
            name = "App",
            packageName = "com.example.app",
            isAppHidden = false
        )
        AppItem(app) {}
    }
}