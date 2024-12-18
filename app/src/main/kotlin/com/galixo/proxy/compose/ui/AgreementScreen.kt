package com.galixo.proxy.compose.ui

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieConstants
import com.galixo.proxy.R
import com.galixo.proxy.compose.navigation.Destinations
import com.galixo.proxy.compose.theme.CatProxyTheme
import com.galixo.proxy.compose.theme.color_light_background
import com.galixo.proxy.compose.theme.color_light_btn_text
import com.galixo.proxy.compose.theme.color_light_primary
import com.galixo.proxy.compose.theme.color_light_text_secondary
import com.galixo.proxy.compose.ui.components.FreeTrialFooter
import com.galixo.proxy.compose.ui.components.LottieAnimation
import com.galixo.proxy.compose.utils.PrefUtils

@Composable
fun AgreementScreen(
    navController: NavController,
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color_light_background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LottieAnimation(
                modifier = Modifier.size(250.dp),
                animationRes = R.raw.anim_splash,
                iterations = LottieConstants.IterateForever
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.agreement_text),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = color_light_text_secondary
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 70.dp)
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                        containerColor = color_light_primary
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = stringResource(R.string.agree_and_continue),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = color_light_btn_text,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                FreeTrialFooter(
                    modifier = Modifier,
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
    CatProxyTheme {
        AgreementScreen(
            navController = rememberNavController(),
            onPrivacyPolicyClick = {},
            onTermsClick = {}
        )
    }
}