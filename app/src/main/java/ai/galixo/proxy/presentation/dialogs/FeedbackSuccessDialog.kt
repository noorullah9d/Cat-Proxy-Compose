package ai.galixo.proxy.presentation.dialogs

import ai.galixo.proxy.R
import ai.galixo.proxy.presentation.components.LottieAnimation
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieConstants

@Composable
fun FeedbackSuccessDialog(
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Surface(
            shape = CatTheme.shape.dialog,
            color = CatTheme.colorScheme.background,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LottieAnimation(
                    modifier = Modifier.size(150.dp),
                    animationRes = R.raw.anim_thumbs_up,
                    iterations = LottieConstants.IterateForever,
                    onAnimationEnd = {}
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Thank You!",
                    style = CatTheme.typography.bold,
                    color = CatTheme.colorScheme.textPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your feedback was successfully submitted.",
                    style = CatTheme.typography.medium,
                    color = CatTheme.colorScheme.textSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onBackToHome,
                    shape = CatTheme.shape.button,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CatTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Back to Home",
                        style = CatTheme.typography.medium,
                        color = CatTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = false)
fun FeedbackDialogPreview() {
    CatTheme {
        FeedbackSuccessDialog(
            modifier = Modifier,
            onBackToHome = {}
        )
    }
}