package com.galixo.proxy.compose.ui.tunneling

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.galixo.proxy.R
import com.galixo.proxy.compose.domain.model.AppInfo
import com.galixo.proxy.compose.theme.CatProxyTheme
import com.galixo.proxy.compose.theme.color_light_divider
import com.galixo.proxy.compose.theme.color_light_primary
import com.galixo.proxy.compose.theme.color_light_text_primary
import com.galixo.proxy.compose.theme.color_light_text_secondary
import com.galixo.proxy.compose.theme.color_light_unselected
import com.galixo.proxy.compose.ui.components.CustomSwitch

@Composable
fun AppItem(
    appInfo: AppInfo,
    onSwitchToggled: (Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
                    style = MaterialTheme.typography.bodyLarge,
                    color = color_light_text_primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = appInfo.packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = color_light_text_secondary
                )
            }

            CustomSwitch(
                checked = !appInfo.isAppHidden,
                onCheckedChange = { isChecked ->
                    onSwitchToggled(!isChecked) // Update isAppHidden state
                },
                thumbColor = Color.White,
                thumbUncheckedColor = Color.White,
                trackCheckedColor = color_light_primary,
                trackUncheckedColor = color_light_unselected
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            color = color_light_divider
        )
    }
}

@Composable
@Preview
fun AppItemPreview() {
    CatProxyTheme {
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