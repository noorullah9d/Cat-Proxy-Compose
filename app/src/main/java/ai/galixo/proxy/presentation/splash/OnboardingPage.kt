package ai.galixo.proxy.presentation.splash

import ai.galixo.proxy.R
import ai.galixo.proxy.presentation.components.LottieAnimation
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieConstants

@Composable
fun OnboardingPage(
    title: String,
    description: String,
    animRes: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(CatTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .padding(8.dp),
            animationRes = animRes,
            iterations = LottieConstants.IterateForever,
            onAnimationEnd = {}
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = CatTheme.typography.extraBold.copy(
                fontSize = 22.sp
            ),
            color = CatTheme.colorScheme.textPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = CatTheme.typography.regularText,
            color = CatTheme.colorScheme.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 52.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun OnboardingPagePreview() {
    CatTheme {
        OnboardingPage(
            title = "Custom VPN Connections",
            description = "Select which apps use the VPN and which bypass it for direct access.",
            animRes = R.raw.anim_onboarding_1
        )
    }
}