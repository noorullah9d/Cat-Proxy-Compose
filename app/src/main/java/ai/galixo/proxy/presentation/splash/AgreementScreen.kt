package ai.galixo.proxy.presentation.splash

import ai.galixo.proxy.R
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.presentation.components.FreeTrialFooter
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.shadowsocks.preference.PrefUtils

@Composable
fun AgreementScreen(
    navController: NavController,
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CatTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*LottieAnimation(
                modifier = Modifier.size(250.dp),
                animationRes = R.raw.anim_splash,
                iterations = LottieConstants.IterateForever
            )*/
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(220.dp),
                    painter = painterResource(id = R.drawable.ic_app_name),
                    contentDescription = "App Logo"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.agreement_text),
                style = CatTheme.typography.regularText,
                color = CatTheme.colorScheme.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        PrefUtils.hasTermsAndConditionsAccepted = true
                        navController.navigate(Destinations.SplashScreen.route) {
                            popUpTo(Destinations.AgreementScreen.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CatTheme.colorScheme.primary
                    ),
                    shape = CatTheme.shape.button
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 10.dp),
                        text = stringResource(R.string.agree_and_continue),
                        style = CatTheme.typography.buttonText,
                        color = CatTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                FreeTrialFooter(
                    modifier = Modifier.padding(bottom = 32.dp),
                    clickableTextColor = CatTheme.colorScheme.primary,
                    textStyle = CatTheme.typography.smallText.copy(
                        color = CatTheme.colorScheme.textSecondary,
                        textAlign = TextAlign.Center
                    ),
                    onPrivacyPolicyClicked = {
                        onPrivacyPolicyClick()
                    },
                    onTermsAndConditionsClicked = {
                        onTermsClick()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgreementPreview() {
    CatTheme {
        AgreementScreen(
            navController = rememberNavController(),
            onPrivacyPolicyClick = {},
            onTermsClick = {}
        )
    }
}