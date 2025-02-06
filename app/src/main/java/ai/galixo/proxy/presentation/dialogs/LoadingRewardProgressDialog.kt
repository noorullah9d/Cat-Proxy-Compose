package ai.galixo.proxy.presentation.dialogs

import ai.galixo.proxy.R
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoadingRewardProgressDialog(
    totalRewards: Int,
    completedRewards: Int,
    onRewardComplete: () -> Unit,
    onDismiss: () -> Unit
) {
    if (completedRewards >= totalRewards) {
        onRewardComplete() // Auto-dismiss when all rewards are done
        onDismiss()
        return
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            shape = CatTheme.shape.dialog,
            color = CatTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth(0.95f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress Bar
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = CatTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = stringResource(id = R.string.showing_rewards),
                    style = CatTheme.typography.itemTitle,
                    color = CatTheme.colorScheme.textPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Progress Text (e.g., "1/3 Rewards Collected")
                Text(
                    text = stringResource(
                        id = R.string.rewards_progress,
                        completedRewards,
                        totalRewards
                    ),
                    style = CatTheme.typography.normal,
                    color = CatTheme.colorScheme.textSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RewardProgressDialogPreview() {
    CatTheme {
        LoadingRewardProgressDialog(
            totalRewards = 3,
            completedRewards = 0,
            onRewardComplete = {},
            onDismiss = {}
        )
    }
}
