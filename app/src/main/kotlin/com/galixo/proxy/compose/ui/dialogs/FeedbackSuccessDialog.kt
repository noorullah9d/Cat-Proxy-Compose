package com.galixo.proxy.compose.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieConstants
import com.galixo.proxy.R
import com.galixo.proxy.compose.theme.CatProxyTheme
import com.galixo.proxy.compose.theme.color_light_background
import com.galixo.proxy.compose.theme.color_light_btn_text
import com.galixo.proxy.compose.theme.color_light_primary
import com.galixo.proxy.compose.theme.color_light_text_primary
import com.galixo.proxy.compose.theme.color_light_text_secondary
import com.galixo.proxy.compose.ui.components.LottieAnimation

@Composable
fun FeedbackSuccessDialog(
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = color_light_background,
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
                    iterations = LottieConstants.IterateForever
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Thank You!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = color_light_text_primary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your feedback was successfully submitted.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = color_light_text_secondary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onBackToHome,
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = color_light_primary
                    )
                ) {
                    Text(
                        text = "Back to Home",
                        style = MaterialTheme.typography.bodyMedium,
                        color = color_light_btn_text
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = false)
fun DialogPreview() {
    CatProxyTheme {
        FeedbackSuccessDialog(
            modifier = Modifier,
            onBackToHome = {}
        )
    }
}