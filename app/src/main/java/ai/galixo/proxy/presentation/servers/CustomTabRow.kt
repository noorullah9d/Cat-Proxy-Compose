package ai.galixo.proxy.presentation.servers

import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTabRow(
    selectedTabIndex: TabPage,
    onTabSelected: (TabPage) -> Unit
) {
    Surface(
        shape = CatTheme.shape.tile,
        color = CatTheme.colorScheme.background,
        border = BorderStroke(1.dp, CatTheme.colorScheme.divider),
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(6.dp)
        ) {
            TabPage.entries.forEach { tab ->
                val isSelected = selectedTabIndex == tab

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clip(CatTheme.shape.ad)
                        .background(if (isSelected) CatTheme.colorScheme.primary else Color.Transparent)
                        .clickable { onTabSelected(tab) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.name,
                        style = CatTheme.typography.medium.copy(
                            fontSize = 15.sp,
                            color = if (isSelected) CatTheme.colorScheme.onPrimary else CatTheme.colorScheme.textSecondary
                        ),
                    )
                }
            }
        }
    }
}

enum class TabPage {
    All, Recommended
}

@Preview(showBackground = true)
@Composable
fun TabRowPreview() {
    CatTheme {
        CustomTabRow(
            selectedTabIndex = TabPage.All,
            onTabSelected = {}
        )
    }
}